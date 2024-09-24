package com.project.messager.domain.websocket;

import com.project.messager.domain.chat.Chat;
import com.project.messager.domain.chat.IChatService;
import com.project.messager.domain.message.IMessageService;
import com.project.messager.domain.message.Message;
import com.project.messager.domain.user.IUserService;
import com.project.messager.domain.user.User;
import com.project.messager.utils.requests.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    private final IMessageService messageService;
    private final IChatService chatService;
    private final IUserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketChatController(IMessageService messageService, IChatService chatService, IUserService userService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest) {
        System.out.println("Received message: " + messageRequest.toMessage());

        // Получаем отправителя и чат по id
        User sender = userService.getUserById(messageRequest.getSenderId());
        Chat chat = chatService.getChat(messageRequest.getChatId());

        // Создаем сообщение и сохраняем его
        Message message = messageRequest.toMessage();
        messageService.addMessage(message, chat, sender);

        // Отправляем сообщение в соответствующий топик
        messagingTemplate.convertAndSend("/topic/chat/" + messageRequest.getChatId(), message);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload MessageRequest messageRequest, SimpMessageHeaderAccessor headerAccessor) {
        // Сохраняем имя пользователя в WebSocket сессии
        headerAccessor.getSessionAttributes().put("username", messageRequest.getSenderId());

        // Создаем сообщение о добавлении пользователя
        Message message = new Message();
        message.setSender(messageRequest.getSenderId());
        message.setText("User joined");

        return message;
    }
}
