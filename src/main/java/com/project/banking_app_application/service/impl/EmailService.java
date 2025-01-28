package com.project.banking_app_application.service.impl;

import com.project.banking_app_application.dto.EmailDetails;

public interface EmailService {
	
	void sendEmailAlert(EmailDetails emailDetails);
}
