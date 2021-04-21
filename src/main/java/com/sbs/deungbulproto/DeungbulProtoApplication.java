package com.sbs.deungbulproto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeungbulProtoApplication extends SpringBootServletInitializer{
		
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {			
			return builder.sources(DeungbulProtoApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(DeungbulProtoApplication.class, args);
		DeungbulProtoApplication app = new DeungbulProtoApplication();
    }

}
