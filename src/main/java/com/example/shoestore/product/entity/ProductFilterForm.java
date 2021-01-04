package com.example.shoestore.product.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterForm {
	
	private List<String> size;
	
	private List<String> category;
	
	private List<String> brand;
	
	private Integer pricelow;
	
	private Integer pricehigh;
	
	private String sort;
	
	private Integer page;
	
	private String search;

}
