package org.example.server.authentication;

import org.example.server.models.User;

import java.util.List;

public class BaseAuthentication implements AuthenticationService {

    private static final List<User> clients = List.of(
            new User("Sergey", "1111","Sewentry"),
            new User("phenix1201", "2222","phenix1201"),
            new User("Max","3333","lukinaro")
    );

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        for (User client : clients){
            if(client.getLogin().equals(login) && client.getPassword().equals(password)){
                return client.getUsername();
            }
        }
        return null;
    }

    @Override
    public void startAuthentication() {
        System.out.println("Старт аутентификации");
    }

    @Override
    public void endAuthentication() {
        System.out.println("Конец аутентификации");
    }
}
