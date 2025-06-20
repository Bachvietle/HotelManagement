package com.example.demo.service;

import com.example.demo.calculator.PriceCalculator;
import com.example.demo.dto.BookingReponseDTO;
import com.example.demo.dto.BookingRequestDTO;
import com.example.demo.dto.CancelReponseDTO;
import com.example.demo.entity.Booking;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.PromoCodeRepository;
import com.example.demo.repository.RoomTypeRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class BookingService {

    private BookingRepository bookingRepository;

    private PromoCodeRepository promoCodeRepository;

    private RoomTypeRepository roomTypeRepository;

    private PriceCalculator priceCalculator;


    // A. Create Booking

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE) // Khi có nhều transaction, các request sẽ đc xử lý tuần tự
    public BookingReponseDTO createBooking (BookingRequestDTO bookingRequestDTO) throws ConflictException {

        // 1. Parse input thời gian
        LocalDate startDate;
        LocalDate endDate;
        OffsetDateTime requestTime;

        try {
            startDate = LocalDate.parse(bookingRequestDTO.getStartDate());
            endDate = LocalDate.parse(bookingRequestDTO.getEndDate());
            requestTime = OffsetDateTime.parse(bookingRequestDTO.getRequestTime());
        } catch (Exception e) {
            throw new BadRequestException("Ngay ko hop le");
        }

        // 2. Validate ngày đặt phòng

        LocalDate toDayUTC = LocalDate.now(ZoneOffset.UTC);

        if (startDate.isBefore(toDayUTC)) {
            throw new BadRequestException("Ngày bắt đầu ko hợp lệ");
        }

        if (endDate.isAfter(startDate)) {
            throw new BadRequestException("Ngày kết thúc ko hợp lệ");
        }

        long dayBetween = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
        if (dayBetween < 1 || dayBetween > 7) {
            throw new BadRequestException("Số ngày đặt phòng phải > 1, < 7");
        }

        // 3. Kểm tra PromoCode

        String promoCode = bookingRequestDTO.getPromoCode();

        if (!promoCode.isEmpty()) { // Nếu empty thì = 0 -> ko cần xử lý nx
            boolean check = promoCodeRepository.existsById(promoCode);
            if (!check) {
                throw new BadRequestException("Mã ko hợp lệ");
            }
        }

        // 4. Kiểm tra phòng trống

        long totalRoom = roomTypeRepository.findByName(bookingRequestDTO.getRoomType())
                .orElseThrow(() -> new BadRequestException("Loại phòng ko tông tại")).getTotalQuantity();

        long countBooking = bookingRepository.countOverlappingBookings(bookingRequestDTO.getRoomType(), startDate, endDate);

        if (countBooking >= totalRoom) {
            throw new ConflictException("Hết phòng loại yêu cầu");
        }

        // 5. Tạo ds ngày thuê (dateList)

        List<LocalDate> dateList = Stream.iterate(startDate, day -> day.plusDays(1)) // Tạo 1 luồng stream bắt đầu từ startDate
                //Mỗi ptu tiếp theo được tạo bằng cách cộng thêm 1 ngày
                .limit(dayBetween) // giới hạn sô ptu
                .toList();


        // 6. Tính baseCost

        long baseCost = priceCalculator.calculatorBaseCost(bookingRequestDTO.getRoomType(), dateList);

        // 7. Tính promoCode (nếu có)

        int promoCodePercent = priceCalculator.getPromoPercent(promoCode);

        long promoDiscount = Math.round((float) (baseCost * promoCodePercent) / 100); // Math.round để làm tròn sát kq nất

        // 8. Tính vipDiscount (nếu có)
        long vipDiscount = 0;
        if (bookingRequestDTO.isVip()) {
            vipDiscount = Math.round((float) (baseCost * 5) / 100);
        }

        // 9. Tính discount3Days, 7Days

        long discount3Days = 0;
        long discount7Days = 0;

        if (dayBetween >= 3 && dayBetween < 7) {
            discount3Days = Math.round((float) (baseCost * 10) / 100);
        }

        if (dayBetween >= 7) {
            discount7Days = Math.round((float) (baseCost * 20) / 100);
        }

        // 10. Tạo detailsCost

        Map<String, Long> detailsCost = new HashMap<>();

        detailsCost.put("base_cost", baseCost);
        detailsCost.put("promo_discount", promoDiscount);
        detailsCost.put("discount_3_days", discount3Days);
        detailsCost.put("discount_7_days", discount7Days);
        detailsCost.put("discount_vip", vipDiscount);

        // 11. Tính totalCost

        long totalCost = baseCost - (promoDiscount + vipDiscount + discount3Days + discount7Days);

        // 12. Tạo bookingID

        String bookingID = "booking_" + UUID.randomUUID().toString(); // tạo ID ramdom đằng sau

        // 13. Tạo và save Booking

        Booking booking = Booking.builder()
                .bookingID(bookingID)
                .userID(bookingRequestDTO.getUserID())
                .roomType(bookingRequestDTO.getRoomType())
                .startDate(startDate)
                .endDate(endDate)
                .isVip(bookingRequestDTO.isVip())
                .promoCode(promoCode)
                .requestTime(requestTime)
                .status("CONFIRMED")
                .baseCost(baseCost)
                .promoDiscount(promoDiscount)
                .discount3Days(discount3Days)
                .discount7Days(discount7Days)
                .vipDiscount(vipDiscount)
                .totalCost(totalCost)
                .build();

        bookingRepository.save(booking);

        // 14. Trả về BookingReponseDTO

        return BookingReponseDTO.builder()
                .bookingID(bookingID)
                .roomType(bookingRequestDTO.getRoomType())
                .startDate(bookingRequestDTO.getStartDate())
                .endDate(bookingRequestDTO.getEndDate())
                .totalCost(totalCost)
                .detailsCost(detailsCost)
                .status("CONFIRMED")
                .message("Đặt phòng thành công")
                .build();

    }

    // B. CancelBooking

    @Transactional(rollbackFor = Exception.class)
    public CancelReponseDTO cancelBooking (String bookingID, String cancelTimeRequest) throws NotFoundException, ForbiddenException {


         // 1. Ktra đã có Booking đó chưa
         Booking booking = bookingRepository.findById(bookingID)
                 .orElseThrow(() -> new NotFoundException("Không tìm thấy đặt phòng"));

         if(booking.getStatus().equals("CANCELED")){
             throw new BadRequestException("Phòng đã được hủy trước đó");
         }

         // 2. Parse tgian hủy
        OffsetDateTime cancelTime;
         try {
           cancelTime = OffsetDateTime.parse(cancelTimeRequest);
         } catch (Exception e){
             throw new BadRequestException("cancel_timestamp không hợp lệ");
         }

        // 3. So sánh tgian Hủy và tgian Bắt đầu đặt phòng

        OffsetDateTime startDateBooking = booking.getStartDate().atStartOfDay().atOffset(ZoneOffset.UTC); // booking.getStartDate() trả về LocalDate nên cần change sang OffsetDate

        if(cancelTime.isAfter(startDateBooking) ){
            throw new ForbiddenException("Ko thể hủy");
        }

        long hoursBetweenCancel = Duration.between(cancelTime, startDateBooking).toHours();

        // 4. Tính tiền phạt và refund

        long penalty;
        long refund;
        String message;

        if(hoursBetweenCancel >= 48) {
            penalty = 0;
            refund = booking.getTotalCost();
            message = "Hủy thành công, hoàn tiền 100%";
        } else {
            penalty = Math.round((float) (booking.getTotalCost() * 50) / 100);
            refund = booking.getTotalCost() - penalty;
            message = "Hủy thành công, hoàn tiền 50%";
        }

        // 5. Cập nhật cancelTime và status

        booking.setStatus("CANCELED");
        booking.setCancelTime(cancelTime);
        bookingRepository.save(booking);

        // 6. ReponseDTO

        return CancelReponseDTO.builder()
                .bookingID(bookingID)
                .penalty(penalty)
                .refundCost(refund)
                .message(message)
                .build();
    }
}
