package com.financeiro.financeiro.controller;

import com.financeiro.financeiro.model.Financa;
import com.financeiro.financeiro.model.User;
import com.financeiro.financeiro.service.FinancaService;
import com.financeiro.financeiro.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/financas")
public class FinancaController {

    private final FinancaService financaService;
    private final UserService userService;

    public FinancaController(FinancaService financaService, UserService userService) {
        this.financaService = financaService;
        this.userService = userService;
    }

    // Adicionar uma nova transação financeira
    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarFinanca(@RequestBody Financa financa, Authentication authentication) {
        String username = authentication.getName(); // Já está autenticado via JWT
        Optional<User> userOpt = userService.buscarPorUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não encontrado.");
        }

        User user = userOpt.get();
        financaService.adicionarFinanca(user, financa);
        return ResponseEntity.ok("Finança adicionada com sucesso.");
    }

    // Listar todas as finanças de um usuário
    @GetMapping("/listar")
    public ResponseEntity<?> listarFinancas(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.buscarPorUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não encontrado.");
        }

        User user = userOpt.get();
        List<Financa> financas = financaService.listarFinancas(user);
        return ResponseEntity.ok(financas);
    }

    // Gerar um resumo financeiro de um usuário
    @GetMapping("/resumo")
    public ResponseEntity<?> gerarResumo(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.buscarPorUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não encontrado.");
        }

        User user = userOpt.get();
        String resumo = financaService.gerarResumo(user);
        return ResponseEntity.ok(resumo);
    }
}
