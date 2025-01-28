package com.project.banking_app_application.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditDebitRequest {

	private String accountNumber;
	private BigDecimal amount;

}
