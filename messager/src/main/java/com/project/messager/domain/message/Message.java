package com.project.messager.domain.message;

import com.project.messager.domain.chat.Chat;
import com.project.messager.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long sender;

    @Column(name = "chat_id")
    private Long chat;

    private String text;
}
