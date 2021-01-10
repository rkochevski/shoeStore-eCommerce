package com.example.shoestore.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shoestore.account.entity.Address;
import com.example.shoestore.account.entity.User;
import com.example.shoestore.mailmessage.service.EmailService;
import com.example.shoestore.product.entity.Order;
import com.example.shoestore.product.entity.Payment;
import com.example.shoestore.product.entity.Shipping;
import com.example.shoestore.product.entity.ShoppingCart;
import com.example.shoestore.product.service.OrderService;
import com.example.shoestore.product.service.ShoppingCartService;

@Controller
public class CheckoutControler {
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	EmailService emailService;

	@GetMapping("/checkout")
	public String showCheckoutPage( @RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
							Model model, Authentication authentication) {		
		User user = (User) authentication.getPrincipal();
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);
		if(shoppingCart.isEmpty()) {
			model.addAttribute("emptyCart", true);
			return "redirect:/shopping-cart/cart";
		}						
		model.addAttribute("cartItemList", shoppingCart.getCartItems());
		model.addAttribute("shoppingCart", shoppingCart);
		if(missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}		
		return "checkout";		
	}
	
	@PostMapping("/checkout")
	public String placeOrder(@ModelAttribute("shipping") Shipping shipping, @ModelAttribute("address") Address address,
							@ModelAttribute("payment") Payment payment, RedirectAttributes redirectAttributes, Authentication authentication) {		
		User user = (User) authentication.getPrincipal();		
		ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(user);	
		if (!shoppingCart.isEmpty()) {
			shipping.setAddress(address);
			Order order = orderService.createOrder(shoppingCart, shipping, payment, user);		
			redirectAttributes.addFlashAttribute("order", order);
			
			emailService.sendOrderConfirmationMail(user, order);
		}
		return "redirect:/order-submitted";
	}
	
	@GetMapping("/order-submitted")
	public String showOrderSubmittedPage(Model model) {
		Order order = (Order) model.asMap().get("order");
		if (order == null) {
			return "redirect:/";
		}
		model.addAttribute("order", order);
		return "orderSubmitted";	
	}

}
