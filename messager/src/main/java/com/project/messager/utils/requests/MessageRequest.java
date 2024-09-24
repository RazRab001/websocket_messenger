package com.project.messager.utils.requests;

import com.project.messager.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageRequest {
    private String text;
    private Long senderId;
    private Long chatId;  // Добавляем поле для идентификатора чата

    public Message toMessage(){
        Message message = new Message();
        message.setText(text);
        message.setChat(chatId); // Устанавливаем ID чата
        return message;
    }
}