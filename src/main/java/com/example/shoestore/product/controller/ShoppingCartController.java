package com.example.shoestore.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shoestore.product.entity.CartItem;
import com.example.shoestore.product.entity.Product;
import com.example.shoestore.product.entity.ShoppingCart;
import com.example.shoestore.product.service.ProductService;
import com.example.shoestore.product.service.ShoppingCartService;
import com.example.shoestore.user.entity.User;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@GetMapping("/cart")
	public String showShoppingCartPage(Model model, Authentication authentication) {		
		User user = (User) authentication.getPrincipal();		
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);		
		model.addAttribute("cartItemList", shoppingCart.getCartItems());
		model.addAttribute("shoppingCart", shoppingCart);		
		return "shoppingCart";
	}

	@PostMapping("/add-item")
	public String addItem(@ModelAttribute("product") Product product, @RequestParam("qty") String qty,
						  @RequestParam("size") String size, RedirectAttributes attributes, Model model, Authentication authentication) {
		product = productService.findProductById(product.getId());				
		if (!product.hasStock(Integer.parseInt(qty))) {
			attributes.addFlashAttribute("notEnoughStock", true);
			return "redirect:/product-detail?id="+product.getId();
		}		
		User user = (User) authentication.getPrincipal();		
		shoppingCartService.addProductToShoppingCart(product, user, Integer.parseInt(qty), size);
		attributes.addFlashAttribute("addProductSuccess", true);
		return "redirect:/product-detail?id="+product.getId();
	}
	
	@PostMapping("/update-item")
	public String updateItemQuantity(@RequestParam("id") Long cartItemId,
									 @RequestParam("qty") Integer qty, Model model) {		
		CartItem cartItem = shoppingCartService.findCartItemById(cartItemId);
		if (cartItem.canUpdateQty(qty)) {
			shoppingCartService.updateCartItem(cartItem, qty);
		}
		return "redirect:/shopping-cart/cart";
	}
	
	@PostMapping("/remove-item")
	public String removeItem(@RequestParam("id") Long id) {		
		shoppingCartService.removeCartItem(shoppingCartService.findCartItemById(id));		
		return "redirect:/shopping-cart/cart";
	}

}
