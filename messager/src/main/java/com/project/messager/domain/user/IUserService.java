package com.project.messager.domain.user;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    User createUser(User user);
    List<User> getUsers(List<Long> userIds);
    User getUserById(Long userId);
    List<User> getUsersByLogin(String login);
}
