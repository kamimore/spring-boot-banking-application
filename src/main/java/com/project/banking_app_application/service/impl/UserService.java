package com.project.banking_app_application.service.impl;

import com.project.banking_app_application.dto.BankResponse;
import com.project.banking_app_application.dto.EnquiryRequest;
import com.project.banking_app_application.dto.UserRequest;

public interface UserService {

	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest request);
	
	String nameEnquiry(EnquiryRequest request);
	
}
