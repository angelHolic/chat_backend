package com.example.demo.controller;

import com.example.demo.dto.request.ChatMessageRequest;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class StompController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    public StompController(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequest request, Principal principal) {
        String username = principal.getName();
        MessageResponse response = chatService.saveMessage(roomId, username, request.getContent());

        simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, response);
    }
}
