package com.example.demo.controller;

import com.example.demo.dto.BookingReponseDTO;
import com.example.demo.dto.BookingRequestDTO;
import com.example.demo.dto.CancelReponseDTO;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@NoArgsConstructor
@Data

@RestController
@RequestMapping("/api/bookings") // prefix chung cho các phương thức trong controller này.

public class BookingController {

    BookingService bookingService;


    /*
     * Endpoint đặt phòng: POST /api/bookings
     */

    @PostMapping
    public ResponseEntity<BookingReponseDTO> createBooking (@Validated @RequestBody BookingRequestDTO bookingRequestDTO) throws ConflictException {

        BookingReponseDTO bookingReponseDTO = bookingService.createBooking(bookingRequestDTO);

        return new ResponseEntity<>(bookingReponseDTO, HttpStatus.CREATED);
    }

    /*
     * Endpoint hủy booking: DELETE /api/bookings/{bookingId}?cancel_timestamp=...
     */

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<CancelReponseDTO> cancelBooking (@PathVariable String bookingID,
                                                           @RequestParam ("cancel_timestamp") String cancelTimeRequest) throws ForbiddenException, NotFoundException {
                                                           // @RequestParam dùng cho các giá trị tuỳ chọn, thông tin phụ trợ (sau ?)

          CancelReponseDTO cancelReponseDTO = bookingService.cancelBooking(bookingID, cancelTimeRequest);

          return new ResponseEntity<>(cancelReponseDTO, HttpStatus.OK);
    }
}