package com.example.shoestore.product.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.shoestore.account.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Date orderDate;
	
	private Date shippingDate;
	
	private String orderStatus;
	
	private BigDecimal orderTotal;
	
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CartItem> cartItems;
	
	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Shipping shipping;
	
	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Payment payment;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

}
