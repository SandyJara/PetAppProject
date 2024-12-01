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

    @Query("SELECT DISTINCT CASE WHEN m.senderUsername = :username THEN m.receiverUsername ELSE m.senderUsername END " +
    	       "FROM Message m WHERE m.senderUsername = :username OR m.receiverUsername = :username")
    	List<String> findDistinctUsersInConversations(@Param("username") String username);

    @Query("SELECT DISTINCT m.receiverUsername FROM Message m WHERE m.senderUsername = :username " +
            "UNION " +
            "SELECT DISTINCT m.senderUsername FROM Message m WHERE m.receiverUsername = :username")
     List<String> findDistinctUsersByUsername(@Param("username") String username);
 }