package com.project.messager.domain.chat;

import com.project.messager.domain.chatUsers.ChatUserId;
import com.project.messager.domain.chatUsers.ChatUsers;
import com.project.messager.domain.chatUsers.ChatUsersRepository;
import com.project.messager.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements IChatService{

    private final ChatRepository repository;
    private final ChatUsersRepository chatUsersRepository;

    @Override
    public Chat createChat(Chat chat, List<User> users) {
        Chat new_chat = repository.save(chat);
        Long chatId = new_chat.getId();
        Set<ChatUsers> chatMembers = new HashSet<>();
        for (User user : users){
            ChatUserId id = new ChatUserId(chatId, user.getId());
            ChatUsers chatMember = chatUsersRepository.save(new ChatUsers(id, new_chat, user));
            chatMembers.add(chatMember);
        }
        new_chat.setMembers(chatMembers);
        return repository.save(new_chat);
    }

    @Override
    public Chat addMember(Long chatId, User user) {
        Chat chat = getChat(chatId);

        if(!chat.getGroupChat()){
            throw new RuntimeException("This action only for group chats");
        }

        Set<ChatUsers> members = chat.getMembers();
        ChatUserId id = new ChatUserId(chatId, user.getId());
        ChatUsers chatMember = chatUsersRepository.save(new ChatUsers(id, chat, user));
        members.add(chatMember);
        chat.setMembers(members);
        return repository.save(chat);
    }

    @Override
    public Chat deleteMember(Long chatId, User member) {
        Chat chat = getChat(chatId);

        if(!chat.getGroupChat()){
            throw new RuntimeException("This action only for group chats");
        }

        Set<ChatUsers> members = chat.getMembers();
        ChatUserId id = new ChatUserId(chatId, member.getId());
        ChatUsers chatMember = chatUsersRepository.findById(id).get();
        members.remove(chatMember);
        chatUsersRepository.delete(chatMember);
        chat.setMembers(members);
        return repository.save(chat);
    }

    @Override
    public Chat getChat(Long chatId) {
        Optional<Chat> op_chat = repository.findById(chatId);
        if(op_chat.isPresent()){
            return op_chat.get();
        } else {
            throw new RuntimeException("Error find chat with ID " + chatId);
        }
    }

    @Override
    public List<Chat> getChatsOfUser(User user) {
        List<ChatUsers> chatUsers = chatUsersRepository.getChatUsersByUser_Id(user.getId());
        return chatUsers.stream().map(ChatUsers::getChat).toList();
    }

    @Override
    public void deleteChat(Long chatId) {
        Chat chat = getChat(chatId);

        Set<ChatUsers> members = chat.getMembers();
        for (ChatUsers member : members){
            ChatUserId id = new ChatUserId(chatId, member.getUser().getId());
            ChatUsers chatMember = chatUsersRepository.findById(id).get();
            members.remove(chatMember);
            chatUsersRepository.delete(chatMember);
        }
        repository.delete(chat);
    }
}
