package com.project.messager.domain.user;

import com.project.messager.loginService.ILoginService;
import com.project.messager.loginService.Login;
import com.project.messager.utils.requests.LoginRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;
    private final ILoginService loginService;

    @PostMapping("")
    public User registration(@RequestBody LoginRequest loginRequest){
        System.out.print(loginRequest.getLogin());
        User user = userService.getUsersByLogin(loginRequest.getLogin()).get(0);
        if(user != null){
            return user;
        }
        return userService.createUser(loginRequest.toUser());
    }

    @GetMapping("/login/{fromId}/{login}")
    public User getUserByLogin(@PathVariable String login, @PathVariable Long fromId){
        User finded_user = userService.getUsersByLogin(login).get(0);
        Login finded_login = loginService.getLogin(fromId, finded_user.getId());
        if(finded_login != null){
            finded_user.setLogin(finded_login.getLogin());
        }
        return finded_user;
    }

    @GetMapping("/id/{fromId}/{id}")
    public User getUserById(@PathVariable Long id, @PathVariable Long fromId){
        User finded_user = userService.getUserById(id);
        Login finded_login = loginService.getLogin(fromId, finded_user.getId());
        if(finded_login != null){
            finded_user.setLogin(finded_login.getLogin());
        }
        return finded_user;
    }

    @PutMapping("/{fromId}/{forId}")
    public Login setLoginForUser(@PathVariable Long fromId, @PathVariable Long forId, @RequestBody String login){
        Login old_login = loginService.getLogin(fromId, forId);
        Login new_login = new Login();
        User fromUser = userService.getUserById(fromId);
        User forUser = userService.getUserById(forId);
        new_login.setWriter(fromId);
        new_login.setOwner(forId);
        new_login.setLogin(login);
        if(old_login == null){
            return loginService.createLogin(fromUser, forUser, new_login);
        }
        return loginService.updateLogin(new_login);
    }
}
