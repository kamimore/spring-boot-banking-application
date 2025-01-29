package com.project.banking_app_application.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.project.banking_app_application.dto.EmailDetails;
import com.project.banking_app_application.entity.Transaction;
import com.project.banking_app_application.entity.User;
import com.project.banking_app_application.repository.TransactionRepository;
import com.project.banking_app_application.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	private TransactionRepository transactionRepository;
	private UserRepository userRepository;
	private EmailService emailService;

	private static final String FILE = "K:\\pdf\\my.pdf";

	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

		List<Transaction> transactionList = transactionRepository.findAll().stream()
				.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isAfter(start)
						|| transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isBefore(end)
						|| transaction.getCreatedAt().isEqual(end))
				.toList();

		return transactionList;
	}

	public List<Transaction> generatedBankStatementPDF(String accountNumber, String startDate, String endDate)
			throws FileNotFoundException, DocumentException {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

		List<Transaction> transactionList = transactionRepository.findAll().stream()
				.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isAfter(start)
						|| transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isBefore(end)
						|| transaction.getCreatedAt().isEqual(end))
				.toList();

		User user = userRepository.findByAccountNumber(accountNumber);
		String customerName = user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName();
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("setting size of document");

		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();

		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("The Java Academy Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);

		PdfPCell bankAddress = new PdfPCell(new Phrase("72, Some Address, Lagos Nigeria"));
		bankAddress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);

		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
		customerInfo.setBorder(0);
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
		stopDate.setBorder(0);
		PdfPCell name = new PdfPCell(new Phrase("CUSTOMER NAME: " + customerName));
		name.setBorder(0);
		PdfPCell space = new PdfPCell();
		space.setBorder(0);
		PdfPCell address = new PdfPCell(new Phrase("Customer Address " + user.getAddress()));
		address.setBorder(0);

		PdfPTable transactionsTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);

		transactionsTable.addCell(date);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(transactionAmount);
		transactionsTable.addCell(status);

		transactionList.forEach(transaction -> {
			transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionType().toString()));
			transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionsTable.addCell(new Phrase(transaction.getStatus().toString()));
		});

		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(stopDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);

		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionsTable);

		document.close();

		EmailDetails emailDetails = EmailDetails.builder().recipient(user.getEmail()).subject("STATEMENT OF ACCOUNT")
				.messageBody("kindly find your requested account statement attached").attachement(FILE).build();
		emailService.sendEmailWithAttachement(emailDetails);

		return transactionList;
	}
}
