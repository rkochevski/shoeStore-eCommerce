package com.example.shoestore.account.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoestore.account.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@EntityGraph(value = "UserComplete", type=EntityGraphType.FETCH)
	User findByUsername(String username);
			
	User findByEmail(String email);

}
