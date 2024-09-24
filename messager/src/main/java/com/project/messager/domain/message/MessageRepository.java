package com.project.messager.domain.message;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> getMessagesByChat(Long chatId);
}
