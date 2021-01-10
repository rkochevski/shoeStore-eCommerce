package com.example.shoestore.mailmessage.service;

import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.shoestore.account.entity.User;
import com.example.shoestore.mailmessage.entity.Message;
import com.example.shoestore.mailmessage.repository.EmailRepository;
import com.example.shoestore.product.entity.Order;

@Service
public class EmailService {
	
	@Autowired
	EmailRepository emailRepository;
	
	@Autowired
	JavaMailSender mailSender;
	
	public void sendAccountCreatedMail(User user) {
		String content = "Dear " + user.getUsername() + "\r\n" + "\r\n" + "Your account has been created!" + "\r\n" + "\r\n" + "Shoe Store, Inc";
		String subject = "Shoe Store - Account Created";
				
		Message message = new Message();
		message.setName(user.getUsername());
		message.setFromEmail("noreply@shoestore.mk");
		message.setToEmail(user.getEmail());
		message.setSubject(subject);
		message.setContent(content);
		
		
		SimpleMailMessage verificationEmail = new SimpleMailMessage();
		verificationEmail.setTo(user.getEmail());
		verificationEmail.setSubject(subject);
		verificationEmail.setText(content);
		
		emailRepository.save(message);
		mailSender.send(verificationEmail);
	}
	
	public void sendOrderConfirmationMail(User user, Order order) {
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		String content = "Dear " + user.getUsername() + "\r\n" + "\r\n" 
				+ "Thank you for your purchase!" + "\r\n" + "\r\n"
				+ "Order number: #ORD" + order.getId() + "\r\n" + "Total: $ " + numberFormat.format(order.getOrderTotal()) + "\r\n" + "\r\n"
				+ "Shoe Store, Inc";
		String subject = "Shoe Store - Order Confirmed";
				
		Message message = new Message();
		message.setName(user.getUsername());
		message.setFromEmail("noreply@shoestore.mk");
		message.setToEmail(user.getEmail());
		message.setSubject(subject);
		message.setContent(content);
		
		
		SimpleMailMessage verificationEmail = new SimpleMailMessage();
		verificationEmail.setTo(user.getEmail());
		verificationEmail.setSubject(subject);
		verificationEmail.setText(content);
		
		emailRepository.save(message);
		mailSender.send(verificationEmail);
	}

}
