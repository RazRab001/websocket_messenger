package com.project.messager.domain.chatUsers;

import com.project.messager.domain.chat.Chat;
import com.project.messager.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"chat_id", "user_id"})})
public class ChatUsers {

    @EmbeddedId
    private ChatUserId id;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
