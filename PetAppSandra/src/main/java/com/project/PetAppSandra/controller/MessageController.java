package com.project.PetAppSandra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.PetAppSandra.Message;
import com.project.PetAppSandra.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send")
    public Message sendMessage(@RequestParam String senderUsername,
                               @RequestParam String receiverUsername,
                               @RequestParam String message) {
        Message newMessage = new Message();
        newMessage.setSenderUsername(senderUsername);
        newMessage.setReceiverUsername(receiverUsername);
        newMessage.setMessage(message);
        newMessage.setSubmissionDate(LocalDateTime.now().toString());
        return messageRepository.save(newMessage);
    }

    @GetMapping("/received")
    public List<Message> getReceivedMessages(@RequestParam String receiverUsername) {
        return messageRepository.findByReceiverUsername(receiverUsername);
    }

    @GetMapping("/sent")
    public List<Message> getSentMessages(@RequestParam String senderUsername) {
        return messageRepository.findBySenderUsername(senderUsername);
    }
}
