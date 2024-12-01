package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Search messages
    List<Message> findByReceiverUsername(String receiverUsername);

    // find messages
    List<Message> findBySenderUsername(String senderUsername);

    // messages between users (by day of submission)
    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderUsername = :user1 AND m.receiverUsername = :user2) " +
           "OR (m.senderUsername = :user2 AND m.receiverUsername = :user1) " +
           "ORDER BY m.submissionDate ASC")
    List<Message> findConversation(@Param("user1") String user1, @Param("user2") String user2);
}
