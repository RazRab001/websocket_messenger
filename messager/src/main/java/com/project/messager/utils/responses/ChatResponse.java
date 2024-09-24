package com.project.messager.utils.responses;

import com.project.messager.domain.chat.Chat;
import com.project.messager.domain.chatUsers.ChatUsers;
import com.project.messager.domain.message.Message;
import com.project.messager.domain.user.User;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChatResponse {
    private Long id;
    private List<User> members;
    private List<Message> messages;

    public ChatResponse(Chat chat, List<Message> messages){
        id = chat.getId();
        members = chat.getMembers().stream()
                .map(ChatUsers::getUser)
                .toList();
        this.messages = messages;
    }
    public ChatResponse(Chat chat){
        id = chat.getId();
        members = chat.getMembers().stream()
                .map(ChatUsers::getUser)
                .toList();
    }
}
