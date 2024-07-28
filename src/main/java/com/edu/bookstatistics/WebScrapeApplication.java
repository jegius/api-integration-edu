package com.edu.bookstatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.edu.bookstatistics")
public class WebScrapeApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebScrapeApplication.class, args);
	}
}