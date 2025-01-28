package com.project.banking_app_application.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.project.banking_app_application.dto.AccountInfo;
import com.project.banking_app_application.dto.BankResponse;
import com.project.banking_app_application.dto.UserRequest;
import com.project.banking_app_application.entity.User;
import com.project.banking_app_application.repository.UserRepository;
import com.project.banking_app_application.utility.AccountUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {

		if (userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE).accountInfo(null).build();
		}

		User newUser = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
				.gender(userRequest.getOtherName()).gender(userRequest.getGender()).address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin()).accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO).email(userRequest.getEmail()).phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber()).status("active").build();

		User savedUser = userRepository.save(newUser);
		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(savedUser.getAccountBalance())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " "
								+ savedUser.getOtherName())
						.accountNumber(savedUser.getAccountNumber()).build())
				.build();

	}

}
