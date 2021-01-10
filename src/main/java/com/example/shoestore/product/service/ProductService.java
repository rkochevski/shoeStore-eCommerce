package com.example.shoestore.product.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.Size;
import com.example.shoestore.product.repository.ProductRepository;
import com.example.shoestore.product.repository.ProductSpecification;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	SizeService sizeService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	CategoryService categoryService;
	
	public String showAddProductPage(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);
		model.addAttribute("allSizes", sizeService.getAllSizes());
		model.addAttribute("allBrands", brandService.getAllBrands());
		model.addAttribute("allCategories", categoryService.getAllCategories());
		return "addProduct";
	}
	
	public void createNewProduct(Product product, HttpServletRequest request) {
		Product newProduct = new Product();
		newProduct.setTitle(product.getTitle());
		newProduct.setCategory(product.getCategory());
		List<String> sizes = Arrays.asList(request.getParameter("size").split("\\s*,\\s*"));
		newProduct = setProductSizes(newProduct, sizes);
		newProduct.setBrand(product.getBrand());
		newProduct.setPrice(product.getPrice());
		newProduct.setStock(product.getStock());
		newProduct.setPicture(product.getPicture());
		newProduct.setEnabled(product.isEnabled());
		saveProduct(newProduct);
	}

	public void editProduct(Product product, HttpServletRequest request) {
		Product updateProduct = new Product();
		updateProduct.setId(product.getId());
		updateProduct.setTitle(product.getTitle());
		updateProduct.setCategory(product.getCategory());
		List<String> sizes = Arrays.asList(request.getParameter("size").split("\\s*,\\s*"));
		updateProduct.setSizes(setProductSizes(product, sizes).getSizes());
		updateProduct.setBrand(product.getBrand());
		updateProduct.setPrice(product.getPrice());
		updateProduct.setStock(product.getStock());
		updateProduct.setPicture(product.getPicture());
		updateProduct.setEnabled(product.isEnabled());
		saveProduct(updateProduct);
	}
	
	public Product setProductSizes(Product product, List<String> sizes) {
		if (sizes != null && !sizes.isEmpty()) {
			Set<Size> sizeElements = new HashSet<>();
			for (String size : sizes) {
				sizeElements.add(new Size(size, product));
			}	
			product.setSizes(sizeElements);
			return product;
		}
		return product;
	}

	public List<Product> findAllProducts() {
		return (List<Product>) productRepository.findAll();
	}

	public Page<Product> findProductsByCriteria(Pageable pageable, Integer priceLow, Integer priceHigh, 
										List<String> sizes, List<String> categories, List<String> brands, String search) {		
		Page<Product> page = productRepository.findAll(ProductSpecification.filterBy(priceLow, priceHigh, sizes, categories, brands, search), pageable);
        return page;		
	}

	public Product findProductById(Long id) {
		Optional<Product> opt = productRepository.findById(id);
		return opt.get();
	}

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	public void deleteProductById(Long id) {
		Product product = productRepository.findById(id).get();
		product.setStock(0);
		product.setEnabled(false);
		saveProduct(product);
	}

	public List<String> getAllSizes() {
		return productRepository.findAllSizes();
	}

	public List<String> getAllCategories() {
		return productRepository.findAllCategories();
	}

	public List<String> getAllBrands() {
		return productRepository.findAllBrands();
	}

}
