package com.project.banking_app_application.utility;

import java.time.Year;

public class AccountUtils {

	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

	public static final String ACCOUNT_CREATION_SUCCESS = "002";
	public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account is created successfully";

	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User is not available with this account number";

	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_CODE_MESSAGE = "User found with this account number";

	public static final String ACCOUNT_CREDITED_CODE = "005";
	public static final String ACCOUNT_CREDITED_CODE_MESSAGE = "User found with this account number";

	public static final String ACCOUNT_DEBITED_CODE = "006";
	public static final String ACCOUNT_DEBITED_CODE_MESSAGE = "Account Balance updated successfully";

	public static final String INSUFFIENT_BALANCE_CODE = "007";
	public static final String INSUFFIENT_BALANCE_CODE_MESSAGE = "Insufficient Balance";

	public static final String SOURCE_ACCOUNT_NOT_EXISTS_CODE = "008";
	public static final String SOURCE_ACCOUNT_NOT_EXISTS_CODE_MESSAGE = "Source Account Code doesn't exists";

	public static final String DESTINATION_ACCOUNT_NOT_EXISTS_CODE = "009";
	public static final String DESTINATION_ACCOUNT_NOT_EXISTS_CODE_MESSAGE = "Destination Account Code doesn't exists";

	public static String generateAccountNumber() {
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;

		int randNumber = (int) (Math.random() * (max - min + 1) + min);

		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();

		return accountNumber.append(year).append(randomNumber).toString();
	}

}
