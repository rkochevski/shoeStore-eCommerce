package com.example.shoestore.account.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shoestore.account.entity.Address;
import com.example.shoestore.account.entity.User;
import com.example.shoestore.account.service.AccountService;
import com.example.shoestore.account.service.UserService;
import com.example.shoestore.product.entity.Order;
import com.example.shoestore.product.service.OrderService;

@Controller
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/login")
	public String showLoginPage(Model model) {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("usernameExists", model.asMap().get("usernameExists"));
		model.addAttribute("emailExists", model.asMap().get("emailExists"));
		return "register";
	}
	
	@GetMapping("/profile")
	public String showUserProfilePage(Model model, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		model.addAttribute("user", user);
		return "profile";
	}
	
	@PostMapping("/create-account")
	public String createNewUser(@Valid @ModelAttribute("user") User user, @ModelAttribute("new-password") String password,
								BindingResult bindingResults, RedirectAttributes redirectAttributes, Model model) {
		return accountService.createAccount(user, password, bindingResults, redirectAttributes, model); 
	}
	
	@PostMapping("/update-profile")
	public String updateUserInfo(@ModelAttribute("user") User user, @RequestParam(name="newPassword", required = false) String newPassword,
								 Model model, Principal principal) throws Exception {
		User currentUser = userService.findByUsername(principal.getName());
		if (currentUser == null) {
			throw new Exception("User not found");
		}
		return accountService.updateUserInfo(currentUser, user, newPassword, model, principal);
	}
	
	@GetMapping("/orders")
	public String showUserOrdersPage(Model model, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		model.addAttribute("user", user);
		List<Order> orders = orderService.findByUser(user);
		model.addAttribute("orders", orders);
		return "orders";
	}
	
	@GetMapping("/order-details")
	public String showOrderDetailsPage(@RequestParam("order") Long id, Model model) {
		Order order = orderService.findOrderWithDetails(id);
		model.addAttribute("order", order);
		return "orderDetails";
	}
	
	@GetMapping("/address")
	public String showUserAddressPage(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "address";
	}
	
	@PostMapping("/update-address")
	public String updateUserAddress(@ModelAttribute("address") Address address, Model model, Principal principal) throws Exception {
		User currentUser = userService.findByUsername(principal.getName());
		if(currentUser == null) {
			throw new Exception ("User not found");
		}
		currentUser.setAddress(address);
		userService.save(currentUser);
		return "redirect:/address";
	}

}
