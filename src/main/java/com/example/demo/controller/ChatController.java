package com.example.demo.controller;

import com.example.demo.dto.request.ChatRoomRequest;
import com.example.demo.dto.response.ChatRoomResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> createRoom(@RequestBody ChatRoomRequest request, @AuthenticationPrincipal String username) {
        ChatRoomResponse response = chatService.createRoom(request, username);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/all")
    public ResponseEntity<List<ChatRoomResponse>> getAllRooms() {
        List<ChatRoomResponse> rooms = chatService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getRooms(@AuthenticationPrincipal String username) {
        List<ChatRoomResponse> rooms = chatService.getRooms(username);
        return  ResponseEntity.ok(rooms);
    }

    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<Void> joinRoom(@PathVariable Long roomId, @AuthenticationPrincipal String username) {
        chatService.joinRoom(roomId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long roomId) {
        List<MessageResponse> messages = chatService.getMessages(roomId);
        return ResponseEntity.ok(messages);
    }

}
