package com.project.PetAppSandra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.PetAppSandra.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverUsername(String receiverUsername);
    List<Message> findBySenderUsername(String senderUsername);
}
