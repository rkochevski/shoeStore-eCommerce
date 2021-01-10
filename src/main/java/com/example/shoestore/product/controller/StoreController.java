package com.example.shoestore.product.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.ProductFilterForm;
import com.example.shoestore.product.service.ProductService;
import com.example.shoestore.product.service.SortFilter;

@Controller
public class StoreController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/store")
	public String showStorePage(@ModelAttribute("filters") ProductFilterForm filters, Model model) {
		Integer page = filters.getPage();			
		int pagenumber = (page == null ||  page <= 0) ? 0 : page-1;
		SortFilter sortFilter = new SortFilter(filters.getSort());
		Page<Product> pageresult = productService.findProductsByCriteria(PageRequest.of(pagenumber,9, sortFilter.getSortType()), 
																filters.getPricelow(), filters.getPricehigh(), 
																filters.getSize(), filters.getCategory(), filters.getBrand(),
																filters.getSearch());	
		model.addAttribute("allCategories", productService.getAllCategories());
		model.addAttribute("allBrands", productService.getAllBrands());
		model.addAttribute("allSizes", productService.getAllSizes());
		model.addAttribute("products", pageresult.getContent());
		model.addAttribute("totalitems", pageresult.getTotalElements());
		model.addAttribute("itemsperpage", 9);
		return "store";
	}
	
	@GetMapping("/store/{category}")
	public String showStorePageByCategory(@PathVariable("category") List<String> category, @ModelAttribute("filters") ProductFilterForm filters, Model model) {
		Integer page = filters.getPage();			
		int pagenumber = (page == null ||  page <= 0) ? 0 : page-1;
		SortFilter sortFilter = new SortFilter(filters.getSort());	
		Page<Product> pageresult = productService.findProductsByCriteria(PageRequest.of(pagenumber,9, sortFilter.getSortType()), 
																filters.getPricelow(), filters.getPricehigh(), 
																filters.getSize(), category, filters.getBrand(),
																filters.getSearch());	
		model.addAttribute("allCategories", productService.getAllCategories());
		model.addAttribute("allBrands", productService.getAllBrands());
		model.addAttribute("allSizes", productService.getAllSizes());
		model.addAttribute("products", pageresult.getContent());
		model.addAttribute("totalitems", pageresult.getTotalElements());
		model.addAttribute("itemsperpage", 9);
		return "store";
	}
	
	
	@GetMapping("/product-detail")
	public String showProductDetailsPage(@PathParam("id") Long id, Model model) {
		Product product = productService.findProductById(id);
		model.addAttribute("product", product);
		model.addAttribute("notEnoughStock", model.asMap().get("notEnoughStock"));
		model.addAttribute("addProductSuccess", model.asMap().get("addProductSuccess"));
		return "productDetails";
	}

}
