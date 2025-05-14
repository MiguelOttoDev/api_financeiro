package com.financeiro.financeiro.controller;

import com.financeiro.financeiro.model.Financa;
import com.financeiro.financeiro.model.User;
import com.financeiro.financeiro.service.FinancaService;
import com.financeiro.financeiro.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/resumo/periodo")
    public ResponseEntity<String> gerarResumoPorPeriodo(
            @RequestParam String periodo,
            @RequestParam String data,
            Authentication auth) {

        String username = auth.getName();
        Optional<User> userOpt = userService.buscarPorUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não autenticado.");
        }

        User user = userOpt.get();
        LocalDate dataReferencia = LocalDate.parse(data);
        String resumo = financaService.gerarResumoPorPeriodo(user, periodo, dataReferencia);
        return ResponseEntity.ok(resumo);
    }



    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editar(@PathVariable int id, @RequestBody Financa novaFinanca, Authentication auth) {
        String username = auth.getName();
        Optional<User> userOpt = userService.buscarPorUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("Usuário não autenticado.");

        boolean sucesso = financaService.editarFinanca(id, novaFinanca, userOpt.get());
        return sucesso ? ResponseEntity.ok("Transação editada.") :
                ResponseEntity.status(404).body("Transação não encontrada.");
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<String> remover(@PathVariable int id, Authentication auth) {
        String username = auth.getName();
        Optional<User> userOpt = userService.buscarPorUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("Usuário não autenticado.");

        boolean removido = financaService.removerFinanca(id, userOpt.get());
        return removido ? ResponseEntity.ok("Removida com sucesso!") :
                ResponseEntity.status(404).body("Transação não encontrada.");
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Financa>> buscar(@RequestParam String descricao, Authentication auth) {
        String username = auth.getName();
        Optional<User> userOpt = userService.buscarPorUsername(username);
        if (userOpt.isEmpty()) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(financaService.buscarPorDescricao(userOpt.get(), descricao));
    }


}
