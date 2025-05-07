package com.financeiro.financeiro.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FinanceiroController {

    @GetMapping("/teste")
    public String teste() {
        return "✅ Acesso autorizado! Você está autenticado com JWT.";
    }
}
