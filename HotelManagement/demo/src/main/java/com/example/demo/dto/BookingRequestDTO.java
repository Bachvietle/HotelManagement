package com.example.demo.dto;

import com.example.demo.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingRequestDTO { // nhận các ttin request từ Clients

    @NotBlank(message = "userID ko được để trống")
    private  String userID;

    @NotBlank(message = "roomType ko được để trống")
    private RoomType roomType; // ở đây có thể thêm cả @Patern để validate

    @NotNull(message = "startDate ko được để trống")
    private String startDate; // ở đây là String mà ở Booking(Entity) là LocalDate nên tí ở Service phải parse

    @NotNull(message = "endDate ko được để trống")
    private String endDate;

    @NotNull(message = "isVip ko được để trống")
    private boolean isVip;

    private String promoCode;

    @NotNull(message = "requestTime ko được để trống")
    private String requestTime;



    // Dùng @notBlank cho String để tránh cả chuỗi rỗng " ". Còn ind, date,... chỉ cần notNull là đủ

    // @Pattern(regexp = "A|B|C") -> validate chuỗi đầu vào có hợp lệ ko (A or B or C)
}
