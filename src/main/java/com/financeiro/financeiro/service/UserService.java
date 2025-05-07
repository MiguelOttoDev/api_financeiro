package com.financeiro.financeiro.service;

import com.financeiro.financeiro.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();

    public boolean cadastrar(User user){
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))){
            return false;
        }
        users.add(user);
        return true;
    }

    public Optional<User> autenticar(String username, String password){
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

}
