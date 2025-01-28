package com.project.banking_app_application.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.banking_app_application.dto.AccountInfo;
import com.project.banking_app_application.dto.BankResponse;
import com.project.banking_app_application.dto.CreditDebitRequest;
import com.project.banking_app_application.dto.EmailDetails;
import com.project.banking_app_application.dto.EnquiryRequest;
import com.project.banking_app_application.dto.TransactionDto;
import com.project.banking_app_application.dto.TransferRequest;
import com.project.banking_app_application.dto.UserRequest;
import com.project.banking_app_application.entity.Transaction;
import com.project.banking_app_application.entity.User;
import com.project.banking_app_application.repository.TransactionRepository;
import com.project.banking_app_application.repository.UserRepository;
import com.project.banking_app_application.utility.AccountUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	TransactionService transactionService;

	@Autowired
	EmailService emailService;

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

		EmailDetails emailDetails = EmailDetails.builder().recipient(userRequest.getEmail()).attachement(null)
				.messageBody("UserCreatedSuccessfully, Your Account Created Successfully")
				.subject("EverthingWentCorrect").build();
		emailService.sendEmailAlert(emailDetails);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(savedUser.getAccountBalance())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " "
								+ savedUser.getOtherName())
						.accountNumber(savedUser.getAccountNumber()).build())
				.build();

	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		// TODO Auto-generated method stub
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_CODE_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " "
								+ foundUser.getOtherName())
						.accountBalance(foundUser.getAccountBalance()).accountNumber(foundUser.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		// TODO Auto-generated method stub
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		}

		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		// TODO Auto-generated method stub
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		foundUser.setAccountBalance(foundUser.getAccountBalance().add(request.getAmount()));

		userRepository.save(foundUser);
		
		//save
		TransactionDto createTransactionDto = TransactionDto.builder().transactionType("CREDIT")
				.accountNumber(request.getAccountNumber()).amount(request.getAmount()).status("SUCCESS")
				.build();
		transactionService.saveTransaction(createTransactionDto);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_CODE_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " "
								+ foundUser.getOtherName())
						.accountBalance(foundUser.getAccountBalance()).accountNumber(foundUser.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		// TODO Auto-generated method stub
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}

		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		if (foundUser.getAccountBalance().compareTo(request.getAmount()) == -1) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFIENT_BALANCE_CODE_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " "
									+ foundUser.getOtherName())
							.accountBalance(foundUser.getAccountBalance()).accountNumber(foundUser.getAccountNumber())
							.build())
					.build();
		}
		foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(request.getAmount()));

		userRepository.save(foundUser);
		
		TransactionDto createTransactionDto = TransactionDto.builder().transactionType("DEBIT")
				.accountNumber(request.getAccountNumber()).amount(request.getAmount()).status("SUCCESS")
				.build();
		transactionService.saveTransaction(createTransactionDto);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_DEBITED_CODE_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " "
								+ foundUser.getOtherName())
						.accountBalance(foundUser.getAccountBalance()).accountNumber(foundUser.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public BankResponse transfer(TransferRequest request) {
		boolean isSourceAccountExists = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
		boolean isDestinationAccountExists = userRepository
				.existsByAccountNumber(request.getDestinationAccountNumber());

		if (!isSourceAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.SOURCE_ACCOUNT_NOT_EXISTS_CODE_MESSAGE).accountInfo(null).build();
		}

		if (!isDestinationAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.DESTINATION_ACCOUNT_NOT_EXISTS_CODE_MESSAGE).accountInfo(null)
					.build();
		}

		User foundSourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		if (foundSourceAccountUser.getAccountBalance().compareTo(request.getAmount()) == -1) {
			return BankResponse.builder().responseCode(AccountUtils.INSUFFIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFIENT_BALANCE_CODE_MESSAGE)
					.accountInfo(AccountInfo.builder().accountName(foundSourceAccountUser.getFirstName() + " "
							+ foundSourceAccountUser.getLastName() + " " + foundSourceAccountUser.getOtherName())
							.accountBalance(foundSourceAccountUser.getAccountBalance())
							.accountNumber(foundSourceAccountUser.getAccountNumber()).build())
					.build();
		}

		foundSourceAccountUser
				.setAccountBalance(foundSourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		userRepository.save(foundSourceAccountUser);
		TransactionDto createTransactionDtoDebit = TransactionDto.builder().transactionType("DEBIT")
				.accountNumber(request.getSourceAccountNumber()).amount(request.getAmount()).status("SUCCESS")
				.build();
		transactionService.saveTransaction(createTransactionDtoDebit);

		User foundDestinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
		foundDestinationAccountUser
				.setAccountBalance(foundDestinationAccountUser.getAccountBalance().add(request.getAmount()));

		userRepository.save(foundDestinationAccountUser);
		TransactionDto createTransactionDtoCredit = TransactionDto.builder().transactionType("CREDIT")
				.accountNumber(request.getDestinationAccountNumber()).amount(request.getAmount()).status("SUCCESS")
				.build();
		transactionService.saveTransaction(createTransactionDtoCredit);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_CODE_MESSAGE)
				.accountInfo(AccountInfo.builder().accountName(foundDestinationAccountUser.getFirstName() + " "
						+ foundDestinationAccountUser.getLastName() + " " + foundDestinationAccountUser.getOtherName())
						.accountBalance(foundDestinationAccountUser.getAccountBalance())
						.accountNumber(foundDestinationAccountUser.getAccountNumber()).build())
				.build();
	}

}
