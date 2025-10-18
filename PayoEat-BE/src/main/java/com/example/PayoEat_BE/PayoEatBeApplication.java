package com.example.PayoEat_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PayoEatBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(PayoEatBeApplication.class, args);
	}
}
