package com.project.messager.domain.message;

import com.project.messager.domain.chat.Chat;
import com.project.messager.domain.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMessageService {
    List<Message> addMessage(Message message, Chat chat, User sender);
    List<Message> getMessagesOfChat(Chat chat);
    void deleteMessage(Message message);
}
