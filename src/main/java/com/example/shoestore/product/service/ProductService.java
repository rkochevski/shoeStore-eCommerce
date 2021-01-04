package com.example.shoestore.product.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.repository.ProductRepository;
import com.example.shoestore.product.repository.ProductSpecification;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Value("${productservice.featured-items-number}")
	private int featuredProductsNumber;

	public List<Product> findAllProducts() {
		return (List<Product>) productRepository.findAll();
	}

	public Page<Product> findProductsByCriteria(Pageable pageable, Integer priceLow, Integer priceHigh, 
										List<String> sizes, List<String> categories, List<String> brands, String search) {		
		Page<Product> page = productRepository.findAll(ProductSpecification.filterBy(priceLow, priceHigh, sizes, categories, brands, search), pageable);
        return page;		
	}	

	public List<Product> findFirstProducts() {
		return productRepository.findAll(PageRequest.of(0,featuredProductsNumber)).getContent(); 
	}

	public Product findProductById(Long id) {
		Optional<Product> opt = productRepository.findById(id);
		return opt.get();
	}

//	@CacheEvict(value = { "sizes", "categories", "brands" }, allEntries = true)
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

//	@CacheEvict(value = { "sizes", "categories", "brands" }, allEntries = true)
	public void deleteProductById(Long id) {
		productRepository.deleteById(id);		
	}

//	@Cacheable("sizes")
	public List<String> getAllSizes() {
		return productRepository.findAllSizes();
	}

//	@Cacheable("categories")
	public List<String> getAllCategories() {
		return productRepository.findAllCategories();
	}

//	@Cacheable("brands")
	public List<String> getAllBrands() {
		return productRepository.findAllBrands();
	}

}
