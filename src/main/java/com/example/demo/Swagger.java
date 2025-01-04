package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class Swagger {
	
	@Bean
	OpenAPI getOpenAPi() {
		Info info = new Info();
		info.setTitle("EasyPay Payroll Management");
		OpenAPI api = new OpenAPI();
		api.setInfo(info);
		return api;
	}
}