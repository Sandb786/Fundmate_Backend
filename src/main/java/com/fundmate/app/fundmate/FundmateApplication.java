package com.fundmate.app.fundmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FundmateApplication 
{
	/*This app is already deployed. For any commits/changes, please update ${...} 
	variables with your own credentials in the application.properties file." */

	public static void main(String[] args) 
	{
		SpringApplication.run(FundmateApplication.class, args);
		System.out.println("\n\nApplication Statted at: 8083");
	}

}

