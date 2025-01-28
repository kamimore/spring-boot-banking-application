package com.project.banking_app_application.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {

	@Schema(
		name="something"
			)
	private String accountName;
	private BigDecimal accountBalance;
	private String accountNumber;
}
