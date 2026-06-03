package com.example.demo.repository;

import com.example.demo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM message WHERE room_id = :roomId ORDER BY created_at ASC", nativeQuery = true)
    List<Message> findAllByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE message SET is_read = true WHERE room_id = :roomId AND sender_id != :userId AND is_read = false", nativeQuery = true)
    void markAllAsRead(@Param("roomId") Long roomId, @Param("userId") Long userId);


}
