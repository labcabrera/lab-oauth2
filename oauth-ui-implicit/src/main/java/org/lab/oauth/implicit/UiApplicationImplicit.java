package org.lab.oauth.implicit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class UiApplicationImplicit extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UiApplicationImplicit.class, args);
	}
}