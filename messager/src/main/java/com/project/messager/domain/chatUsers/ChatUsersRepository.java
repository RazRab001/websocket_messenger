package com.project.messager.domain.chatUsers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatUsersRepository extends JpaRepository<ChatUsers, ChatUserId> {
    List<ChatUsers> getChatUsersByUser_Id(Long userId);
}
