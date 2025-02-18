package com.project.banking_app_application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.banking_app_application.dto.BankResponse;
import com.project.banking_app_application.dto.CreditDebitRequest;
import com.project.banking_app_application.dto.EnquiryRequest;
import com.project.banking_app_application.dto.LoginRequest;
import com.project.banking_app_application.dto.TransferRequest;
import com.project.banking_app_application.dto.UserRequest;
import com.project.banking_app_application.entity.User;
import com.project.banking_app_application.repository.UserRepository;
import com.project.banking_app_application.service.impl.JwtUtil;
import com.project.banking_app_application.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "some text")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Operation(summary = "something idk", description = "some description")
	@ApiResponse(responseCode = "201", description = "some description")
	@PostMapping
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}

	@PostMapping("/login")
	public String login(@RequestBody LoginRequest request) {
		System.out.println("📌 Login request received for email: " + request.getEmail());
		User user = userRepository.findByEmail(request.getEmail());

		if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return "Invalid email or password!";
		}

		return jwtUtil.generateToken(request.getEmail());
	}

	@PostMapping("/balance-enquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request) {
		System.out.println("📌 Login request received for email: ");
		return userService.balanceEnquiry(request);
	}

	@PostMapping("/name-enquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}

	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request) {
		return userService.creditAccount(request);
	}

	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}

	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest request) {
		return userService.transfer(request);
	}
}
