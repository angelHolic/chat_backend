package com.example.demo.dto.response;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long id;
    private String senderUsername;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public MessageResponse(Long id, String senderUsername, String content, boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
