package com.example.shoestore.product.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shoestore.product.entity.Product;
import com.example.shoestore.account.entity.User;
import com.example.shoestore.product.entity.CartItem;
import com.example.shoestore.product.entity.Order;
import com.example.shoestore.product.entity.Payment;
import com.example.shoestore.product.entity.Shipping;
import com.example.shoestore.product.entity.ShoppingCart;
import com.example.shoestore.product.repository.ProductRepository;
import com.example.shoestore.product.repository.CartItemRepository;
import com.example.shoestore.product.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ProductRepository productRepository;
			
	@Transactional
	@CacheEvict(value = "itemcount", allEntries = true)
	public synchronized Order createOrder(ShoppingCart shoppingCart, Shipping shipping, Payment payment, User user) {
		Order order = createNewOrder(shoppingCart, shipping, payment, user);
		setCartItems(shoppingCart, order);
		return order;	
	}

	private Order createNewOrder(ShoppingCart shoppingCart, Shipping shipping, Payment payment, User user) {
		Order order = new Order();
		order.setUser(user);
		order.setPayment(payment);
		order.setShipping(shipping);
		order.setOrderTotal(shoppingCart.getGrandTotal());
		
		shipping.setOrder(order);
		payment.setOrder(order);
		
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate = today.plusDays(5);
		
		order.setOrderDate(Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		order.setShippingDate(Date.from(estimatedDeliveryDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		order.setOrderStatus("In Progress");
		
		order = orderRepository.save(order);
		return order;
	}
	
	private void setCartItems(ShoppingCart shoppingCart, Order order) {
		List<CartItem> cartItems = shoppingCart.getCartItems();
		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			product.decreaseStock(item.getQty());
			productRepository.save(product);
			item.setOrder(order);
			cartItemRepository.save(item);
		}
	}

	public Order findOrderWithDetails(Long id) {
		return orderRepository.findEagerById(id);
	}	

	public List<Order> findByUser(User user) {
		return orderRepository.findByUser(user);
	}

}
