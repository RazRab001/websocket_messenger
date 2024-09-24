package com.project.messager.config;

import com.project.messager.domain.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Long userId = (Long) headerAccessor.getSessionAttributes().get("username");
        if (userId != null) {
            log.info("User Disconnected: " + userId);

            // Отправляем сообщение о выходе пользователя из чата
            Message leaveMessage = new Message();
            leaveMessage.setSender(userId);
            leaveMessage.setText("User left");

            messagingTemplate.convertAndSend("/topic/public", leaveMessage);
        }
    }
}
