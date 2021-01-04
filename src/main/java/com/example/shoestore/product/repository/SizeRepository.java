package com.example.shoestore.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoestore.product.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

}
