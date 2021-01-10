package com.example.shoestore;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.shoestore.account.service.UserService;

@Component
public class ShoeStoreAppStartupRunner implements CommandLineRunner {
	
	@Autowired
	private UserService userService;

	@Override
	public void run(String... args) throws Exception {
		userService.createUser("admin", "password", "admin@shoestore.com", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
	}

}
