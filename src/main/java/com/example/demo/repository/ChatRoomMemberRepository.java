package com.example.demo.repository;

import com.example.demo.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query(value = "select * from chat_room_member where room_id = :room_id", nativeQuery = true)
    List<ChatRoomMember> findByRoom(@Param("room_id") Long roomId);

    @Query(value = "select * from chat_room_member where room_id = :roomId and user_id = :userId", nativeQuery = true)
    Optional<ChatRoomMember> findByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(*) > 0 FROM chat_room_member WHERE room_id = :roomId AND user_id = :userId", nativeQuery = true)
    boolean existsByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

}
