package com.project.messager.utils.requests;

import com.project.messager.domain.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private List<Long> memberIds;
    private Boolean groupChat;

    public Chat toChat(){
        Chat chat = new Chat();
        if(memberIds.size() > 2){
            groupChat = true;
        } else {
            groupChat = false;
        }
        chat.setGroupChat(groupChat);
        return chat;
    }
}
