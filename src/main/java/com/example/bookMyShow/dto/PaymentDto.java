package com.example.bookMyShow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    private String transactionId;
    private String userId;
    private Double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String status;

}
