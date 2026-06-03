package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room_member")
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public ChatRoom getChatRoom() { return chatRoom; }
    public User getUser() { return user; }
    public LocalDateTime getJoinedAt() { return joinedAt; }

    public void setChatRoom(ChatRoom chatRoom) { this.chatRoom = chatRoom; }
    public void setUser(User user) { this.user = user; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

}
// 이 entity가 main entity