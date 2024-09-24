package com.project.messager.domain.chat;

import com.project.messager.domain.message.IMessageService;
import com.project.messager.domain.message.Message;
import com.project.messager.domain.user.IUserService;
import com.project.messager.domain.user.User;
import com.project.messager.domain.user.UserRepository;
import com.project.messager.utils.requests.ChatRequest;
import com.project.messager.utils.requests.MessageRequest;
import com.project.messager.utils.responses.ChatResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@AllArgsConstructor
public class ChatController {
    private final IUserService userService;
    private final IChatService chatService;
    private final IMessageService messageService;

    @PostMapping("")
    public ChatResponse createNewChat(@RequestBody ChatRequest chatRequest){
        List<Long> memberIds = chatRequest.getMemberIds();
        List<User> members = userService.getUsers(memberIds);
        if(members.size() < 2){
            throw new RuntimeException("Wrong members count: " + members.size());
        }
        Chat chat = chatService.createChat(chatRequest.toChat(), members);
        return new ChatResponse(chat);
    }

    @PutMapping("/{chatId}")
    public ChatResponse addMemberToChat(@RequestBody Long member, @PathVariable Long chatId){
        User newMember = userService.getUserById(member);
        return new ChatResponse(chatService.addMember(chatId, newMember));
    }

    @DeleteMapping("/{chatId}")
    public ChatResponse removeMemberFromChat(@RequestBody Long member, @PathVariable Long chatId){
        User newMember = userService.getUserById(member);
        return new ChatResponse(chatService.deleteMember(chatId, newMember));
    }

    @PutMapping("/message/{chatId}")
    public ChatResponse sendMessage(@RequestBody MessageRequest messageRequest, @PathVariable Long chatId){
        User sender = userService.getUserById(messageRequest.getSenderId());
        Chat chat = chatService.getChat(chatId);
        List<Message> messages = messageService.addMessage(messageRequest.toMessage(), chat, sender);

        return new ChatResponse(chat, messages);
    }

    @GetMapping("/of/{userId}")
    public List<ChatResponse> getChatsOfUser(@PathVariable Long userId){
        User member = userService.getUserById(userId);
        List<Chat> chats = chatService.getChatsOfUser(member);
        List<ChatResponse> response = new ArrayList<>();
        for(Chat chat : chats){
            List<Message> messages = messageService.getMessagesOfChat(chat);
            response.add(new ChatResponse(chat, messages));
        }
        return response;
    }
    @GetMapping("/{chatId}")
    public ChatResponse getChatById(@PathVariable Long chatId){
        Chat chat = chatService.getChat(chatId);
        List<Message> messages = messageService.getMessagesOfChat(chat);
        return new ChatResponse(chat, messages);
    }
}
