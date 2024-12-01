package com.project.PetAppSandra.repository;

import com.project.PetAppSandra.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Buscar mensajes recibidos por un usuario específico
    List<Message> findByReceiverUsername(String receiverUsername);

    // Buscar mensajes enviados por un usuario específico
    List<Message> findBySenderUsername(String senderUsername);

    // Buscar mensajes entre dos usuarios específicos, ordenados por fecha
    @Query("SELECT m FROM Message m WHERE " +
           "(m.senderUsername = :user1 AND m.receiverUsername = :user2) " +
           "OR (m.senderUsername = :user2 AND m.receiverUsername = :user1) " +
           "ORDER BY m.submissionDate ASC")
    List<Message> findConversation(@Param("user1") String user1, @Param("user2") String user2);
}
