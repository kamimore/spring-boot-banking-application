package com.project.banking_app_application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.banking_app_application.dto.TransactionDto;
import com.project.banking_app_application.entity.Transaction;
import com.project.banking_app_application.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(TransactionDto transaction) {
		Transaction createTransaction = Transaction.builder().transactionType(transaction.getTransactionType())
				.accountNumber(transaction.getAccountNumber()).amount(transaction.getAmount()).status("SUCCESS")
				.build();

		transactionRepository.save(createTransaction);

	}

}
