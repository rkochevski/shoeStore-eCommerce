package com.example.shoestore.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.shoestore.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true")
	List<Product> findAll();
		
	@EntityGraph(attributePaths = { "sizes", "category", "brand" })
	Optional<Product> findById(Long id);
	
	@Query("SELECT DISTINCT s.value FROM Size s")
	List<String> findAllSizes();
	
	@Query("SELECT DISTINCT c.name FROM Category c")
	List<String> findAllCategories();
	
	@Query("SELECT DISTINCT b.name FROM Brand b")
	List<String> findAllBrands();

}
