package com.financeiro.financeiro.service;

import com.financeiro.financeiro.model.Conta;
import com.financeiro.financeiro.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> users = new ArrayList<>();
    private List<Conta> contas = new ArrayList<>();

    public boolean cadastrar(User user){
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))){
            return false;
        }
        users.add(user);

        Conta conta = new Conta("Conta de " + user.getUsername(), 0.0, user);
        contas.add(conta);

        return true;
    }

    public Optional<User> autenticar(String username, String password){
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public Conta buscarContaPorUsuario(User user) {
        return contas.stream().filter(c -> c.getUser().equals(user)).findFirst().orElse(null);
    }

    public Optional<User> buscarPorUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }


}
