package com.example.shoestore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.shoestore.product.entity.Brand;
import com.example.shoestore.product.entity.Category;
import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.Size;
import com.example.shoestore.product.repository.ProductRepository;
import com.example.shoestore.product.repository.ProductSpecification;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {
	
	@Autowired
	TestEntityManager entityManager;
	 
	@Autowired
	ProductRepository productRepository;
	
	@Before
	public void setUp() {

		// Create product 1
		Product product1 = new Product();
		product1.setTitle("product1");
		Category category1 = new Category();
		category1.setName("Mens");
		entityManager.persist(category1);
		product1.setCategory(category1);

		List<String> sizes1 = Arrays.asList("38","39");
		Set<Size> sizeElements1 = new HashSet<>();
		for (String size : sizes1) {
			sizeElements1.add(new Size(size, product1));
		}	
		product1.setSizes(sizeElements1);
		
		Brand brand1 = new Brand();
		brand1.setName("Adidas");
		entityManager.persist(brand1);
		product1.setBrand(brand1);
		
		product1.setPrice(100);
		product1.setStock(10);
		product1.setPicture("/images/product1");
		product1.setEnabled(true);
		entityManager.persist(product1);
		
		
		// Create product 2
		Product product2 = new Product();
		product2.setTitle("product2");
		Category category2 = new Category();
		category2.setName("Ladies");
		entityManager.persist(category2);
		product2.setCategory(category2);

		List<String> sizes2 = Arrays.asList("39","40", "41");
		Set<Size> sizeElements2 = new HashSet<>();
		for (String size : sizes2) {
			sizeElements2.add(new Size(size, product2));
		}	
		product2.setSizes(sizeElements2);
		
		Brand brand2 = new Brand();
		brand2.setName("Nike");
		entityManager.persist(brand2);
		product2.setBrand(brand2);
		
		product2.setPrice(200);
		product2.setStock(20);
		product2.setPicture("/images/product2");
		product2.setEnabled(true);
		entityManager.persist(product2);
		
		
		// Create product 3
		Product product3 = new Product();
		product3.setTitle("product3");
		product3.setCategory(category2);

		List<String> sizes3 = Arrays.asList("41","42", "43");
		Set<Size> sizeElements3 = new HashSet<>();
		for (String size : sizes3) {
			sizeElements3.add(new Size(size, product3));
		}	
		product3.setSizes(sizeElements3);
		
		Brand brand3 = new Brand();
		brand3.setName("Puma");
		entityManager.persist(brand3);
		product3.setBrand(brand3);
		
		product3.setPrice(300);
		product3.setStock(30);
		product3.setPicture("/images/product3");
		product3.setEnabled(true);
		entityManager.persist(product3);
		
		
		// Create product 4
		Product product4 = new Product();
		product4.setTitle("product4");
		Category category3 = new Category();
		category3.setName("Kids");
		entityManager.persist(category3);
		product4.setCategory(category3);

		List<String> sizes4 = Arrays.asList("43","44", "45");
		Set<Size> sizeElements4 = new HashSet<>();
		for (String size : sizes4) {
			sizeElements4.add(new Size(size, product4));
		}	
		product4.setSizes(sizeElements4);
		
		product4.setBrand(brand1);
		
		product4.setPrice(400);
		product4.setStock(40);
		product4.setPicture("/images/product4");
		product4.setEnabled(true);
		entityManager.persist(product4);
	}
	
	@Test
	public void find_all_distinct_categories() {		
        assertThat(productRepository.findAllCategories()).hasSize(3).contains("Mens", "Ladies", "Kids");	
	}
	
	@Test
	public void find_all_distinct_sizes() {		
        assertThat(productRepository.findAllSizes()).hasSize(8).contains("38", "39", "40", "41", "42", "43", "44", "45");        
	}
	
	@Test
	public void find_all_distinct_brands() {		
        assertThat(productRepository.findAllBrands()).hasSize(3).contains("Adidas", "Nike", "Puma");        
	}
	
	@Test
	public void filter_products_between_prices() {
		int low = 150;
		int high = 350;
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(low, high, null, null, null, null));
		assertThat(results).hasSize(2).extracting("title").contains("product2", "product3");
	}
	
	@Test
	public void filter_products_with_price_higher_than_number() {
		int low = 300;
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(low, null, null, null, null, null));
		assertThat(results).hasSize(2).extracting("title").contains("product3", "product4");
	}
	
	@Test
	public void filter_products_with_price_lower_than_number() {
		int high = 200;
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(null, high, null, null, null, null));
		assertThat(results).hasSize(2).extracting("title").contains("product1", "product2");
	}
	
	@Test
	public void filter_products_by_category() {
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(null, null, null, Arrays.asList("Ladies", "Kids"), null, null));
		List<Product> results2 = productRepository.findAll(ProductSpecification.filterBy(null, null, null, Arrays.asList("Mens", "notARealCategory"), null, null));
		assertThat(results).hasSize(3).extracting("title").contains("product2", "product3", "product4");
		assertThat(results2).hasSize(1).extracting("title").contains("product1");
	}
	
	@Test
	public void filter_products_by_size() {
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(null, null, Arrays.asList("43", "45", "48"), null, null, null));
		List<Product> results2 = productRepository.findAll(ProductSpecification.filterBy(null, null, Arrays.asList("38"), null, null, null));
		assertThat(results).hasSize(2).extracting("title").contains("product3", "product4");
		assertThat(results2).hasSize(1).extracting("title").contains("product1");
	}
	
	@Test
	public void filter_products_by_brand() {
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(null, null, null, null, Arrays.asList("Adidas"), null));
		List<Product> results2 = productRepository.findAll(ProductSpecification.filterBy(null, null, null, null, Arrays.asList("Puma", "notARealBrand"), null));
		assertThat(results).hasSize(2).extracting("title").contains("product1", "product4");
		assertThat(results2).hasSize(1).extracting("title").contains("product3");
	}
	
	@Test
	public void filter_products_by_search_term() {
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(null, null, null, null, null, "product"));
		List<Product> results2 = productRepository.findAll(ProductSpecification.filterBy(null, null, null, null, null, "uct4"));
		List<Product> results3 = productRepository.findAll(ProductSpecification.filterBy(null, null, null, null, null, "unmatchingterm"));
		assertThat(results).hasSize(4).extracting("title").contains("product1", "product2", "product3", "product4");
		assertThat(results2).hasSize(1).extracting("title").contains("product4");
		assertThat(results3).isEmpty();
	}
	
	@Test
	public void find_all_if_all_filters_are_null() {
		List<Product> results = productRepository.findAll(ProductSpecification.filterBy(null, null, null, null, null, null));
		assertThat(results).hasSize(4).extracting("title").contains("product1", "product2", "product3", "product4");
	}

}
