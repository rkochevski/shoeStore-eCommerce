package com.example.shoestore.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shoestore.product.entity.Brand;
import com.example.shoestore.product.repository.BrandRepository;

@Service
public class BrandService {
	
	@Autowired
	BrandRepository brandRepository;

	public List<Brand> getAllBrands() {
		return brandRepository.findAll();
	}

}
