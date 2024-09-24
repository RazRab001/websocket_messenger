package com.project.messager.loginService;

import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<Login, Long> {
    Login getLoginByWriterAndOwner(Long writer, Long owner);
}
