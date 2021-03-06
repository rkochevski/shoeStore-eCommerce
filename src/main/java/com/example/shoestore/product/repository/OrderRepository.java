package com.example.shoestore.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoestore.account.entity.User;
import com.example.shoestore.product.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findByUser(User user); 
	
	@EntityGraph(attributePaths = {"cartItems", "payment", "shipping"})
	Order findEagerById(Long id);
	
}
