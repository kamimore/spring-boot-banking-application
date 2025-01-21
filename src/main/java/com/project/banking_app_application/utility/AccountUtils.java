package com.project.banking_app_application.utility;

import java.time.Year;

public class AccountUtils {

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
