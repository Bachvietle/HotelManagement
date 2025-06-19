package com.example.demo.dto;

import com.example.demo.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingReponseDTO {

    private String bookingID;

    private RoomType roomType;

    private String startDate;

    private String endDate;

    private Long totalCost;

    private Map<String, Long> detailsCost; // chi tiết về base cost và các giảm giá. VD : "baseCosst": ..., "promoCode": ...

    private String status;

    private String message; // "Đặt phòng thành công"
}
