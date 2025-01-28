package com.project.banking_app_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
	
	private String recipient;
	private String messageBody;
	private String subject;
	private String attachement;
}
