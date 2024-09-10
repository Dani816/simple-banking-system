package com.danijel.bank_app_leapwise;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankAppLeapwiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAppLeapwiseApplication.class, args);
		System.out.println("I'm working");
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull())
				.setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper;
	}
}
