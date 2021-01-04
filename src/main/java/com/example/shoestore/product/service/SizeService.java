package com.example.shoestore.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shoestore.product.repository.SizeRepository;

@Service
public class SizeService {
	
	@Autowired
	SizeRepository sizeRepository;

	public Object getAllSizes() {
		return sizeRepository.findAll();
	}

}
