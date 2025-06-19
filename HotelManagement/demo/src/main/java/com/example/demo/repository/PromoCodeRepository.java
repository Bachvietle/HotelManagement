package com.example.demo.repository;

import com.example.demo.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
    //dùng findByName để ktra mã đúng hay ko
}
