package com.project.messager.utils.requests;

import com.project.messager.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {
    private String login;
    private String password;
    public User toUser(){
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }
}
