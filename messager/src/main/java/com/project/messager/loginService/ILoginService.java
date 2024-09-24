package com.project.messager.loginService;

import com.project.messager.domain.user.User;

public interface ILoginService {
    Login createLogin(User loginFrom, User loginFor, Login login);
    Login getLogin(Long loginFromId, Long loginForId);
    Login updateLogin(Login login);
}
