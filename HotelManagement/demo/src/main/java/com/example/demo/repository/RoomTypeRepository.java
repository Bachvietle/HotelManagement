package com.example.demo.repository;

import com.example.demo.entity.RoomType;
import com.example.demo.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Long> {
    Optional<RoomTypeEntity> findByName(RoomType name);

    // Dùng để lấy totalQuantity của mỗi loại phòng -> rồi so sánh với số booking hiện tại -> số booking < số lượng phòng => còn phòng

    // dùng optional để ko trả về null trực tiếp (return optional.empty())
    // => ép dev phải xử lý TH ko tìm thấy (bằng orElseThrow ném EXxception)
}
