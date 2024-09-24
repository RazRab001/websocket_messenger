package com.project.messager.domain.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService{
    private final UserRepository repository;
    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> getUsers(List<Long> userIds) {
        return repository.findUsersByIdIn(userIds);
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> op_user = repository.findById(userId);
        if (op_user.isPresent()){
            return op_user.get();
        } else {
            throw new RuntimeException("Error find user with ID " + userId);
        }
    }

    @Override
    public List<User> getUsersByLogin(String login) {
        return repository.findUsersByLogin(login);
    }
}
