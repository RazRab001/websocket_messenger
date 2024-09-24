package com.project.messager.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findUsersByIdIn(List<Long> ids);
    List<User> findUsersByLogin(String login);
}
