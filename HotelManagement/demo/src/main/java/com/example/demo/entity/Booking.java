package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Booking {

    @Id // đánh dấu khóa chính
    @Column(name = "booking_id",length = 50)
    private String bookingID;

    @Column(name = "user_id", nullable = false)
    private String userID;

    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_vip", nullable = false)
    private boolean isVip;

    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "request_time", nullable = false)
    private OffsetDateTime requestTime;

    @Column(name = "status", nullable = false)
    private String status; // CONFIRMED or  CANCELED

    @Column(name = "base_cost", nullable = false)
    private Long baseCost;

    @Column(name = "promocode_discount")
    private Long promoDiscount;

    @Column(name = "discount_3days", nullable = false)
    private Long discount3Days;

    @Column(name = "discount_7days", nullable = false)
    private Long discount7Days;

    @Column(name = "vip_discount", nullable = false)
    private Long vipDiscount;

    @Column(name = "total_cost", nullable = false)
    private Long totalCost;

    @Column(name = "cancel_time")
    private OffsetDateTime cancelTime;
}
