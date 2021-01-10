package com.example.shoestore.account.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.shoestore.account.entity.Role;
import com.example.shoestore.account.entity.User;
import com.example.shoestore.account.entity.UserRole;
import com.example.shoestore.account.repository.RoleRepository;
import com.example.shoestore.account.repository.UserRepository;
import com.example.shoestore.security.SecurityUtility;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	public boolean checkIfUsernameExists(@Valid User user, RedirectAttributes redirectAttributes, boolean invalidFields) {
		if (findByUsername(user.getUsername()) != null) {
			redirectAttributes.addFlashAttribute("usernameExists", true);
			invalidFields = true;
		}
		return invalidFields;
	}
	
	public boolean checkIfEmailExists(@Valid User user, RedirectAttributes redirectAttributes, boolean invalidFields) {
		if (findByEmail(user.getEmail()) != null) {
			redirectAttributes.addFlashAttribute("emailExists", true);
			invalidFields = true;
		}
		return invalidFields;
	}

	@Transactional
	public User createUser(String username, String password, String email, List<String> roles) {
		User user = findByUsername(username);
		if (user != null) {
			return user;
		} else {
			user = createNewUser(username, password, email);
			Set<UserRole> userRoles = createUserRoles(user, roles);		
			user.setUserRoles(userRoles);
			return userRepository.save(user);
		}
	}

	public User findById(Long id) {
		Optional<User> opt = userRepository.findById(id);
		return opt.get();
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void save(User user) {
		userRepository.save(user);
	}
	
	private User createNewUser(String username, String password, String email) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(SecurityUtility.passwordEncoder().encode(password));
		user.setEmail(email);
		return user;
	}
	
	private Set<UserRole> createUserRoles(User user, List<String> roles) {
		Set<UserRole> userRoles = new HashSet<>();
		for (String rolename : roles) {
			Role role = roleRepository.findByName(rolename);
			if (role == null) {
				role = new Role();
				role.setName(rolename);
				roleRepository.save(role);
			}
			userRoles.add(new UserRole(user, role));
		}	
		return userRoles;
	}

	public boolean isUsernameAvailable(User user, User currentUser, Model model) {
		User existingUser = findByUsername(user.getUsername());
		if (existingUser != null && !existingUser.getId().equals(currentUser.getId()))  {
			model.addAttribute("usernameExists", true);
			return false;
		}
		return true;
	}

	public boolean isEmailAvailable(User user, User currentUser, Model model) {
		User existingUser = findByEmail(user.getEmail());
		if (existingUser != null && !existingUser.getId().equals(currentUser.getId()))  {
			model.addAttribute("emailExists", true);
			return false;
		}
		return true;
	}

	public boolean isPasswordCorrect(String newPassword, User user, User currentUser, Model model) {
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			String dbPassword = currentUser.getPassword();
			if(passwordEncoder.matches(user.getPassword(), dbPassword)){
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			} else {
				model.addAttribute("incorrectPassword", true);
				return false;
			}
		}
		return true;
	}

	public User updateUserInfo(User currentUser, User user) {
		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());		
		save(currentUser);
		return currentUser;
	}

}
