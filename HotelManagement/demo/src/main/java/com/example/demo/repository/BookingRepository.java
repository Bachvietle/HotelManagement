package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT COUNT(b) FROM Booking b" +    // ở đây "b" là 1 đtg của class Booking (entity Booking)
            "WHERE b.roomType = :roomType" +     // roomtype là 1  trong các filed của đtg
            " AND b.status = :'CONFIRMED'" +
            " AND NOT (b.endDate < :startDate OR b.startDate > :endDate)")

    long countOverlappingBookings(@Param("roomType")RoomType roomType,
                                  @Param("startDate")LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);


    /*
    - @Query giúp ta tự custom câu lệnh SQL thay vì dùng các hàm có sẵn(findAll,..)

    - @Param giúp ánh xạ biến :roomType với tham ssoos truyền vào(roomType)

    => @Query sẽ định ngĩa hàm đó làm gì
       @Param sẽ truyền tham số
     */
}
