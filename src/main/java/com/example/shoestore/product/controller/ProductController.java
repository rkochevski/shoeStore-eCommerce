package com.example.shoestore.product.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shoestore.product.entity.Brand;
import com.example.shoestore.product.entity.Category;
import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.ProductCreator;
import com.example.shoestore.product.entity.Size;
import com.example.shoestore.product.service.BrandService;
import com.example.shoestore.product.service.CategoryService;
import com.example.shoestore.product.service.ProductService;
import com.example.shoestore.product.service.SizeService;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	SizeService sizeService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/add")
	public String showAddProductPage(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		model.addAttribute("allSizes", sizeService.getAllSizes());
		model.addAttribute("allBrands", brandService.getAllBrands());
		model.addAttribute("allCategories", categoryService.getAllCategories());
		return "addProduct";
	}
	
	@PostMapping("/create-product")
	public String createNewProduct(@ModelAttribute("product") Product product, HttpServletRequest request) {
		Product newProduct = new ProductCreator()
				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
				.create();
		newProduct.setTitle(product.getTitle());
		newProduct.setStock(product.getStock());
		newProduct.setPrice(product.getPrice());
		newProduct.setPicture(product.getPicture());
		newProduct.setBrand(product.getBrand());
		newProduct.setCategory(product.getCategory());
		
		productService.saveProduct(newProduct);	
		return "redirect:product-list";
	}
	
//	@PostMapping("/create-product")
//	public String addProduct(@ModelAttribute("product") Product product, HttpServletRequest request) {
//		Product newProduct = new ProductCreator()
//				.withTitle(product.getTitle())
//				.stockAvailable(product.getStock())
//				.withPrice(product.getPrice())
//				.imageLink(product.getPicture())
//				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
//				.ofCategories(Arrays.asList(request.getParameter("category").split("\\s*,\\s*")))
//				.ofBrand(Arrays.asList(request.getParameter("brand").split("\\s*,\\s*")))
//				.create();		
//		productService.saveProduct(newProduct);	
//		return "redirect:product-list";
//	}
	
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
		String preselectedBrands = product.getBrand().getName();
//		for (Brand brand : product.getBrand()) {
//			preselectedBrands += (brand.getName() + ",");
//		}
		String preselectedCategories = product.getCategory().getName();
//		for (Category category : product.getCategories()) {
//			preselectedCategories += (category.getName() + ",");
//		}		
		model.addAttribute("product", product);
		model.addAttribute("preselectedSizes", preselectedSizes);
		model.addAttribute("preselectedBrands", preselectedBrands);
		model.addAttribute("preselectedCategories", preselectedCategories);
		model.addAttribute("allSizes", productService.getAllSizes());
		model.addAttribute("allBrands", productService.getAllBrands());
		model.addAttribute("allCategories", productService.getAllCategories());
		return "editProduct";
	}
	
	@PostMapping("/edit")
	public String editProduct(@ModelAttribute("product") Product product, HttpServletRequest request) {		
		Product newProduct = new ProductCreator()
				.sizesAvailable(Arrays.asList(request.getParameter("size").split("\\s*,\\s*")))
				.create();
		newProduct.setTitle(product.getTitle());
		newProduct.setStock(product.getStock());
		newProduct.setPrice(product.getPrice());
		newProduct.setPicture(product.getPicture());
		newProduct.setBrand(product.getBrand());
		newProduct.setCategory(product.getCategory());
		newProduct.setId(product.getId());
		productService.saveProduct(newProduct);	
		return "redirect:product-list";
	}
	
	@PostMapping("/delete")
	public String deleteProduct(@RequestParam("id") Long id) {
		productService.deleteProductById(id);
		return "redirect:product-list";
	}

}
