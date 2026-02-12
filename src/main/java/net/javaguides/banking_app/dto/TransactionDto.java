package net.javaguides.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Long accountId;
    private String transactionType;
    private double amount;
    private Long targetAccountId;
    private String description;
    private LocalDateTime createdAt;
    private double balanceAfter;
}