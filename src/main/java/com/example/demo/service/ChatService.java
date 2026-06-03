package com.example.demo.service;

import com.example.demo.dto.request.ChatRoomRequest;
import com.example.demo.dto.response.ChatRoomResponse;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.ChatRoomMember;
import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.repository.ChatRoomMemberRepository;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRoomMemberRepository chatRoomMemberRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberRepository = chatRoomMemberRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public ChatRoomResponse createRoom(ChatRoomRequest request, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ChatRoom room = new ChatRoom();
        room.setName(request.getName());
        chatRoomRepository.save(room);

        ChatRoomMember member = new ChatRoomMember();
        member.setChatRoom(room);
        member.setUser(user);
        chatRoomMemberRepository.save(member);

        return new ChatRoomResponse(room.getId(), room.getName(), room.getCreatedAt());
    }

    public List<ChatRoomResponse> getAllRooms() {
        return chatRoomRepository.findAll().stream()
                .map(r -> new ChatRoomResponse(r.getId(), r.getName(), r.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<ChatRoomResponse> getRooms(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return chatRoomRepository.findByUserId(user.getId()).stream()
                .map(r -> new ChatRoomResponse(r.getId(), r.getName(), r.getCreatedAt()))
                .collect(Collectors.toList());


    }

    public void joinRoom(Long roomId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        if(chatRoomMemberRepository.findByRoomIdAndUserId(roomId, user.getId()).isPresent()) {
            throw new RuntimeException("이미 참여 중인 채팅방입니다.");
        }

        ChatRoomMember member = new ChatRoomMember();
        member.setChatRoom(room);
        member.setUser(user);
        chatRoomMemberRepository.save(member);
    }

    public List<MessageResponse> getMessages(Long roomId) {
        return messageRepository.findAllByRoomId(roomId).stream()
                .map(m -> new MessageResponse(m.getId(), m.getSender().getUsername(), m.getContent(), m.isRead(), m.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public MessageResponse saveMessage(Long roomId, String username, String content) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Message message = new Message();
        message.setChatRoom(room);
        message.setSender(user);
        message.setContent(content);
        messageRepository.save(message);

        return new MessageResponse(message.getId(), username, content, false, message.getCreatedAt());
    }

}
