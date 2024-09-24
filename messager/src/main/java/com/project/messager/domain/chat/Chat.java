package com.project.messager.domain.chat;

import com.project.messager.domain.chatUsers.ChatUsers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private Set<ChatUsers> members = new HashSet<>();

    private Boolean groupChat;

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
