package com.example.shoestore.mailmessage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shoestore.mailmessage.entity.Message;

@Repository
public interface EmailRepository extends JpaRepository<Message, Long> {

}
