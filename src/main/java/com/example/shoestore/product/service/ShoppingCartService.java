package com.example.shoestore.product.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.CartItem;
import com.example.shoestore.product.entity.ShoppingCart;
import com.example.shoestore.product.repository.CartItemRepository;
import com.example.shoestore.user.entity.User;

@Service
public class ShoppingCartService {
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	public ShoppingCart getShoppingCart(User user) {
		return new ShoppingCart(cartItemRepository.findAllByUserAndOrderIsNull(user));
	}

	@Cacheable("itemcount")
	public int getItemsNumber(User user) {
		return cartItemRepository.countDistinctByUserAndOrderIsNull(user);
	}

	public CartItem findCartItemById(Long cartItemId) {
		Optional<CartItem> optional = cartItemRepository.findById(cartItemId);
		return optional.get();
	}

	@CacheEvict(value = "itemcount", allEntries = true)
	public CartItem addProductToShoppingCart(Product product, User user, int qty, String size) {
		ShoppingCart shoppingCart = this.getShoppingCart(user);
		CartItem cartItem = shoppingCart.findCartItemByProductAndSize(product.getId(), size);
		if (cartItem != null && cartItem.hasSameSizeThan(size)) {
			cartItem.addQuantity(qty);
			cartItem.setSize(size);
			cartItem = cartItemRepository.save(cartItem);
		} else {
			cartItem = new CartItem();
			cartItem.setUser(user);
			cartItem.setProduct(product);
			cartItem.setQty(qty);
			cartItem.setSize(size);
			cartItem = cartItemRepository.save(cartItem);
		}		
		return cartItem;	
	}

	@CacheEvict(value = "itemcount", allEntries = true)
	public void removeCartItem(CartItem cartItem) {
		cartItemRepository.deleteById(cartItem.getId());
	}

	@CacheEvict(value = "itemcount", allEntries = true)
	public void updateCartItem(CartItem cartItem, Integer qty) {
		if (qty == null || qty <= 0) {
			this.removeCartItem(cartItem);
		} else if (cartItem.getProduct().hasStock(qty)) {
			cartItem.setQty(qty);
			cartItemRepository.save(cartItem);
		}
	}

	@CacheEvict(value = "itemcount", allEntries = true)
	public void clearShoppingCart(User user) {
		cartItemRepository.deleteAllByUserAndOrderIsNull(user);	
	}	

}
