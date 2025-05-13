package com.financeiro.financeiro.service;

import com.financeiro.financeiro.model.Conta;
import com.financeiro.financeiro.model.Financa;
import com.financeiro.financeiro.model.TipoFinanca;
import com.financeiro.financeiro.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FinancaService {

    private List<Financa> financas = new ArrayList<>();
    private UserService userService;

    public FinancaService(UserService userService) {
        this.userService = userService;
    }

    public String adicionarFinanca(User user, Financa financa) {
        Conta conta = userService.buscarContaPorUsuario(user);
        if (conta == null) {
            return "Usuário não encontrado ou conta não associada.";
        }

        // Define o nome do usuário na Financa
        financa.setNome(user.getUsername());

        // Define o ID da transação (usando contador interno)
        financa.setId(Financa.getProximoId());

        // Atualiza o saldo com base no tipo da transação
        if (financa.getTipo() == TipoFinanca.RECEITA) {
            conta.setSaldo(conta.getSaldo() + financa.getValor());
        } else if (financa.getTipo() == TipoFinanca.DESPESA) {
            conta.setSaldo(conta.getSaldo() - financa.getValor());
        }

        // Adiciona a finança à lista
        financas.add(financa);
        return "Transação registrada com sucesso!";
    }

    public List<Financa> listarFinancas(User user) {
        List<Financa> financasUsuario = new ArrayList<>();
        for (Financa financa : financas) {
            if (financa.getNome().equals(user.getUsername())) {
                financasUsuario.add(financa);
            }
        }
        return financasUsuario;
    }

    public String gerarResumo(User user) {
        Conta conta = userService.buscarContaPorUsuario(user);
        if (conta == null) {
            return "Usuário não encontrado ou conta não associada.";
        }

        double totalEntradas = 0.0;
        double totalSaidas = 0.0;

        for (Financa financa : financas) {
            if (financa.getNome().equals(user.getUsername())) {
                if (financa.getTipo() == TipoFinanca.RECEITA) {
                    totalEntradas += financa.getValor();
                } else if (financa.getTipo() == TipoFinanca.DESPESA) {
                    totalSaidas += financa.getValor();
                }
            }
        }

        double saldoAtual = totalEntradas - totalSaidas;

        return "Resumo de " + user.getUsername() + ":\n" +
                "Entradas: " + totalEntradas + "\n" +
                "Saídas: " + totalSaidas + "\n" +
                "Saldo Atual: " + saldoAtual;
    }
}
