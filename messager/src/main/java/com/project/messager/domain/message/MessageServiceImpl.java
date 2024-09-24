package com.project.messager.domain.message;

import com.project.messager.domain.chat.Chat;
import com.project.messager.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService{
    private final MessageRepository repository;
    @Override
    public List<Message> addMessage(Message message, Chat chat, User sender) {
        Message new_message = repository.save(message);
        new_message.setChat(chat.getId());
        new_message.setSender(sender.getId());
        repository.save(message);
        return getMessagesOfChat(chat);
    }

    @Override
    public List<Message> getMessagesOfChat(Chat chat) {
        return repository.getMessagesByChat(chat.getId());
    }

    @Override
    public void deleteMessage(Message message) {
        repository.delete(message);
    }
}
