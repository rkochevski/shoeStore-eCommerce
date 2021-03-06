package com.example.shoestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shoestore.product.service.CategoryService;

@Controller
public class ViewController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/")
	public String homePage() {
		return "index";
	}
	
	@GetMapping("/privacy-policy")
	public String privacyPolicyPage() {
		return "privacy-policy";
	}
	
	@GetMapping("/terms")
	public String termsAndConditionsPage() {
		return "terms-and-conditions";
	}

}
