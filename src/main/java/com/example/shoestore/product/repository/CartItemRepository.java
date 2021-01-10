package com.example.shoestore.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoestore.account.entity.User;
import com.example.shoestore.product.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	@EntityGraph(attributePaths = {"product"})
	List<CartItem> findAllByUserAndOrderIsNull(User user);
	
	void deleteAllByUserAndOrderIsNull(User user);

	int countDistinctByUserAndOrderIsNull(User user);

}
