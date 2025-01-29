package com.project.banking_app_application.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.project.banking_app_application.dto.BankResponse;
import com.project.banking_app_application.dto.CreditDebitRequest;
import com.project.banking_app_application.dto.EnquiryRequest;
import com.project.banking_app_application.dto.TransferRequest;
import com.project.banking_app_application.dto.UserRequest;
import com.project.banking_app_application.entity.Transaction;
import com.project.banking_app_application.service.impl.BankStatement;
import com.project.banking_app_application.service.impl.TransactionService;
import com.project.banking_app_application.service.impl.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TransactionController {

	@Autowired
	BankStatement bankStatement;

	@GetMapping("/bank-statement")
	public List<Transaction> generatedBankStatement(@RequestParam String accountNumber, @RequestParam String startDate,
			@RequestParam String endDate) {

		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}
	
	@GetMapping("/generate-pdf")
	public List<Transaction> generatedBankStatementPDF(@RequestParam String accountNumber, @RequestParam String startDate,
			@RequestParam String endDate) throws FileNotFoundException, DocumentException {

		return bankStatement.generatedBankStatementPDF(accountNumber, startDate, endDate);
	}
}
