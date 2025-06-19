package com.example.demo.calculator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateWeekend {

    boolean isWeekend (LocalDate date){

        DayOfWeek dw = date.getDayOfWeek();

        return dw == DayOfWeek.SATURDAY || dw == DayOfWeek.SUNDAY;
    }
}
