package com.example.shoestore.product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.Size;
import com.example.shoestore.product.service.BrandService;
import com.example.shoestore.product.service.CategoryService;
import com.example.shoestore.product.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/add")
	public String showAddProductPage(Model model) {
		return productService.showAddProductPage(model);
	}
	
	@PostMapping("/create-product")
	public String createNewProduct(@ModelAttribute("product") Product product, HttpServletRequest request) {
		productService.createNewProduct(product, request);
		return "redirect:product-list";
	}
	
	@GetMapping("/product-list")
	public String showProductListPage(Model model) {
		List<Product> products = productService.findAllProducts();
		model.addAttribute("products", products);
		return "productList";
	}
	
	@GetMapping("/edit")
	public String showEditProductPage(@RequestParam("id") Long id, Model model) {
		Product product = productService.findProductById(id);
		String preselectedSizes = "";
		for (Size size : product.getSizes()) {
			preselectedSizes += (size.getValue() + ",");
		}
		model.addAttribute("product", product);
		model.addAttribute("preselectedSizes", preselectedSizes);
		model.addAttribute("allBrands", brandService.getAllBrands());
		model.addAttribute("allCategories", categoryService.getAllCategories());
		return "editProduct";
	}
	
	@PostMapping("/edit")
	public String editProduct(@ModelAttribute("product") Product product, HttpServletRequest request) {
		productService.editProduct(product, request);
		return "redirect:product-list";
	}
	
	@GetMapping("/delete")
	public String deleteProduct(@RequestParam("id") Long id) {
		productService.deleteProductById(id);
		return "redirect:product-list";
	}

}
