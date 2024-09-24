package com.project.messager.domain.chat;

import com.project.messager.domain.user.User;

import java.util.List;

public interface IChatService {
    Chat createChat(Chat chat, List<User> users);
    Chat addMember(Long chatId, User user);
    Chat deleteMember(Long chatId, User member);
    Chat getChat(Long chatId);
    List<Chat> getChatsOfUser(User user);
    void deleteChat(Long chatId);
}
