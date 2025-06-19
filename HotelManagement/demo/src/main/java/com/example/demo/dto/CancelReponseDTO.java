package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CancelReponseDTO {

   private String bookingID;

   private Long refundCost; // hoàn trả

   private Long penalty; // phạt

   private String message; // hủy thành công, hoàn trả bnh

}
