package com.financeiro.financeiro.service;

import com.financeiro.financeiro.model.Conta;
import com.financeiro.financeiro.model.Financa;
import com.financeiro.financeiro.model.TipoFinanca;
import com.financeiro.financeiro.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        financa.setNome(user.getUsername());
        financa.setId(Financa.getProximoId());

        if (financa.getTipo() == TipoFinanca.RECEITA) {
            conta.setSaldo(conta.getSaldo() + financa.getValor());
        } else if (financa.getTipo() == TipoFinanca.DESPESA) {
            conta.setSaldo(conta.getSaldo() - financa.getValor());
        }

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

    public String gerarResumoPorData(User user, LocalDate dataInicio, LocalDate dataFim) {
        double entradas = 0.0;
        double saidas = 0.0;

        for (Financa f : financas) {
            if (f.getNome().equals(user.getUsername())) {
                LocalDate data = LocalDate.parse(f.getData());
                if (!data.isBefore(dataInicio) && !data.isAfter(dataFim)) {
                    if (f.getTipo() == TipoFinanca.RECEITA) {
                        entradas += f.getValor();
                    } else {
                        saidas += f.getValor();
                    }
                }
            }
        }

        return "Entradas: " + entradas + "\nSaídas: " + saidas + "\nSaldo: " + (entradas - saidas);
    }

    public String gerarResumoPorPeriodo(User user, String tipoPeriodo, LocalDate dataReferencia) {
        LocalDate inicio;
        LocalDate fim;

        switch (tipoPeriodo.toLowerCase()) {
            case "dia":
                inicio = dataReferencia;
                fim = dataReferencia;
                break;
            case "semana":
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                inicio = dataReferencia.with(weekFields.dayOfWeek(), 1);
                fim = dataReferencia.with(weekFields.dayOfWeek(), 7);
                break;
            case "mes":
                inicio = dataReferencia.withDayOfMonth(1);
                fim = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());
                break;
            case "ano":
                inicio = dataReferencia.withDayOfYear(1);
                fim = dataReferencia.withDayOfYear(dataReferencia.lengthOfYear());
                break;
            default:
                return "Tipo de período inválido. Use: dia, semana, mes, ano.";
        }

        return gerarResumoPorData(user, inicio, fim);
    }

    public boolean editarFinanca(int id, Financa novaFinanca, User user) {
        for (int i = 0; i < financas.size(); i++) {
            Financa f = financas.get(i);
            if (f.getId() == id && f.getNome().equals(user.getUsername())) {
                novaFinanca.setId(id);
                novaFinanca.setNome(user.getUsername());
                financas.set(i, novaFinanca);
                return true;
            }
        }
        return false;
    }

    public boolean removerFinanca(int id, User user) {
        return financas.removeIf(f -> f.getId() == id && f.getNome().equals(user.getUsername()));
    }

    public List<Financa> buscarPorDescricao(User user, String descricao) {
        return financas.stream()
                .filter(f -> f.getNome().equals(user.getUsername()) && f.getDescricao().toLowerCase().contains(descricao.toLowerCase()))
                .toList();
    }
}
