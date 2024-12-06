package com.project.PetAppSandra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.PetAppSandra.Message;
import com.project.PetAppSandra.repository.MessageRepository;
import com.project.PetAppSandra.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    // Seng a message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam String senderUsername,
                                         @RequestParam String receiverUsername,
                                         @RequestParam String message) {
        System.out.println("Sender: " + senderUsername + ", Receiver: " + receiverUsername + ", Message: " + message);

        boolean receiverExists = userRepository.existsByUsername(receiverUsername);
        if (!receiverExists) {
            return ResponseEntity.badRequest().body(Map.of("error", "The recipient does not exist."));
        }

        try {
            Message newMessage = new Message();
            newMessage.setSenderUsername(senderUsername);
            newMessage.setReceiverUsername(receiverUsername);
            newMessage.setMessage(message);
            newMessage.setSubmissionDate(LocalDateTime.now().toString());

            messageRepository.save(newMessage);

            return ResponseEntity.ok(Map.of("status", "Message sent successfully"));
        } catch (Exception e) {
            System.out.println("Error while saving the message: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error"));
        }
    }

    // Get messages received
    @GetMapping("/received")
    public List<Message> getReceivedMessages(@RequestParam String receiverUsername) {
        return messageRepository.findByReceiverUsername(receiverUsername);
    }

    // Get messages sent
    @GetMapping("/sent")
    public List<Message> getSentMessages(@RequestParam String senderUsername) {
        return messageRepository.findBySenderUsername(senderUsername);
    }

    // Get a conversation between 2 users
    @GetMapping("/conversation")
    public List<Message> getConversation(@RequestParam String user1, @RequestParam String user2) {
        return messageRepository.findConversation(user1, user2);
    }
    
    @GetMapping("/conversations/users")
    public ResponseEntity<List<String>> getConversationUsers(@RequestParam String username) {
        
    	// Consult database to get the users (not repeating them)
        List<String> conversationUsers = messageRepository.findDistinctUsersByUsername(username);

        if (conversationUsers.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 if the is no data
        }
        return ResponseEntity.ok(conversationUsers); // Users list
    }
}
