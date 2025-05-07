package com.financeiro.financeiro.controller;

import com.financeiro.financeiro.model.User;
import com.financeiro.financeiro.security.JwtUtil;
import com.financeiro.financeiro.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        boolean sucesso = userService.cadastrar(user);
        if (!sucesso) {
            return ResponseEntity.badRequest().body("Usuário já existe.");
        }
        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.autenticar(user.getUsername(), user.getPassword())
                .map(u -> ResponseEntity.ok(jwtUtil.gerarToken(u.getUsername())))
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas"));
    }

}
