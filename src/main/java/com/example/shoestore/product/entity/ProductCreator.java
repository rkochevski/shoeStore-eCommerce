package com.example.shoestore.product.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductCreator {
	
	private String title;
	
	private int stock;	
	
	private double price;
	
	private String picture;
	
	private List<String> sizes;
	
	private String category;
	
	private String brand;
	
	public ProductCreator withTitle(String title) {
		this.title = title;
		return this;
	}
	
	public ProductCreator stockAvailable(int stock) {
		this.stock = stock;
		return this;
	}
	
	public ProductCreator withPrice(double price) {
		this.price = price;
		return this;
	}
	
	public ProductCreator imageLink(String picture) {
		this.picture = picture;
		return this;
	}
	
	public ProductCreator sizesAvailable(List<String> sizes) {
		this.sizes = sizes;
		return this;
	}
	
	public ProductCreator ofCategories(String category) {
		this.category = category;
		return this;
	}
	
	public ProductCreator ofBrand(String brand) {
		this.brand = brand;
		return this;
	}
	
	public Product create() {
		Product product = new Product();
//		product.setTitle(this.title);
//		product.setPrice(this.price);
//		product.setStock(this.stock);
//		product.setPicture(this.picture);
		
		if (this.sizes != null && !this.sizes.isEmpty()) {
			Set<Size> sizeElements = new HashSet<>();
			for (String value : this.sizes) {
				sizeElements.add(new Size(value, product));
			}	
			product.setSizes(sizeElements);
		}
		
//		if (this.categories != null && !this.categories.isEmpty() ) {
//			Set<Category> catElements = new HashSet<>();
//			for (String value : this.categories) {
//				catElements.add(new Category(value, product));
//			}
//			product.setCategories(catElements);
//		}
//		
//		if (this.brands != null && !this.brands.isEmpty() ) {
//			Set<Brand> brandElements = new HashSet<>();
//			for (String value : this.brands) {
//				brandElements.add(new Brand(value, product));
//			}
//			product.setBrands(brandElements);
//		}		
		
		return product;
	}

}
