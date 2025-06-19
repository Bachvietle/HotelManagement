package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promo_code")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PromoCode {

    @Id
    @Column(name = "code", length = 50)
    private String nameCode;

    @Column(name = "discount_percent", nullable = false)
    private Integer discountPercent;
}
