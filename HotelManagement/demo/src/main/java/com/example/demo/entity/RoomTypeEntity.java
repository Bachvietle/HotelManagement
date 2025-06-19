package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_type")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomTypeEntity { // class này để ánh xạ lưu trữ số phòng từ DB nhằm MĐ ktra xem còn bnh phòng trống

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)  // lưu vào DB dưới dạng String
    private RoomType name; // STANDARD, DELUXE, SUITE

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

}
