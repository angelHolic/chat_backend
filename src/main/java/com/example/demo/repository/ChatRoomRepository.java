package com.example.demo.repository;

import com.example.demo.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository <ChatRoom, Long> {

    @Query(value = " select cr.* from chat_room cr " +
            " join chat_room_member crm on cr.id = crm.room_id " +
            " where crm.user_id = :userId ", nativeQuery = true)
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

}
