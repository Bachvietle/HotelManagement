package com.example.demo.calculator;

import com.example.demo.entity.PromoCode;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.HolidayRepository;
import com.example.demo.repository.PromoCodeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


// Package calculator này giúp tính toán nững thứ ruờm rà trước khi vào Service

public class PriceCalculator {

   private HolidayRepository holidayRepository;

   private DateWeekend dw;

   private PromoCodeRepository promoCodeRepository;

   private static final long STANDARD_NORMAL = 100_000;
   private static final long STANDARD_WEEKEND = 150_000;
   private static final long STANDARD_HOLIDAY = 200_000;

   private static final long DELUXE_NORMAL = 200_000;
   private static final long DELUXE_WEEKEND = 250_000;
   private static final long DELUXE_HOLIDAY = 300_000;

   private static final long SUITE_NORMAL = 350_000;
   private static final long SUITE_WEEKEND = 400_000;
   private static final long SUITE_HOLIDAY = 500_000;


   // Tính baseCost chưa giảm giá
   public long calculatorBaseCost (RoomType roomType, List<LocalDate> dateList){

      long sum = 0;

      for (LocalDate day : dateList){

         boolean isHoliday = holidayRepository.existByHolidayDate(day);

         boolean isWeekend = dw.isWeekend(day);

         switch (roomType){

            case STANDARD:
               if(isHoliday) sum += STANDARD_HOLIDAY;
               else if (isWeekend) sum += STANDARD_WEEKEND;
               else sum += STANDARD_NORMAL;
               break;

            case  SUITE:
               if(isHoliday) sum += SUITE_HOLIDAY;
               else if (isWeekend) sum += SUITE_WEEKEND;
               else sum += SUITE_NORMAL;
               break;

            case DELUXE:
               if(isHoliday) sum += DELUXE_HOLIDAY;
               else if (isWeekend) sum += DELUXE_WEEKEND;
               else sum += DELUXE_NORMAL;
               break;
         }
      }
      return sum;
   }

  public int getPromoPercent(String code){

      if (code.isEmpty()){
         return 0;
      }

      return promoCodeRepository.findById(code)
              .map(PromoCode::getDiscountPercent) // tương đương: .map(promoCodeObj -> promoCodeObj.getDiscountPercent())
              .orElse(0);

      // nếu tìm thấy mã -> get luôn % giảm của mã

      // Cú pháp: Optional<T>.map(Function<T, R>) → Optional<R>
  }
}
