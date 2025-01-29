package com.project.banking_app_application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

@RestController
@RequestMapping("/jwt/token")
public class JwtTokenGeneratorController {

	@GetMapping
	public String generateJwtToken() throws NoSuchAlgorithmException{
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Base64 Secret Key: " + base64Key);
		return base64Key;
        
    } 
}
