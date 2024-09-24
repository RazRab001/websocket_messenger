package com.project.messager.loginService;

import com.project.messager.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements ILoginService{

    private final LoginRepository repository;
    @Override
    public Login createLogin(User loginFrom, User loginFor, Login login) {
        login.setWriter(loginFrom.getId());
        login.setOwner(loginFor.getId());
        return repository.save(login);
    }

    @Override
    public Login getLogin(Long loginFromId, Long loginForId) {
        return repository.getLoginByWriterAndOwner(loginFromId, loginForId);
    }

    @Override
    public Login updateLogin(Login login) {
        Login old_login = getLogin(login.getWriter(), login.getOwner());
        if(old_login == null) return null;

        old_login.setLogin(login.getLogin());
        return repository.save(old_login);
    }
}
