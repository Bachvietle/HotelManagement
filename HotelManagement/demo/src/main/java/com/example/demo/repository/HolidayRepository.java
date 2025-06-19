package com.example.demo.repository;

import com.example.demo.entity.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayRepository extends JpaRepository<Holidays, Long> {
    boolean existByHolidayDate(LocalDate date);


    /* (Method name convention):

    - existByXyz (...): ktra sự tồn tại

    Trong đó: Xyz là tên field trong class T (T trong JpaRepository<T, X>)

   => HolidayDate là field holidayDate trong class Holidays

   => Spring sẽ tìm trong Cột holidayDate của Bảng Holisays có gtri date hay ko

     */

    // Ảo ma vc =))))
}
