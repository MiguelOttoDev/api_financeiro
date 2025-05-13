package com.financeiro.financeiro.model;
import java.util.List;

public class Resumo {
    private double totalEntradas;
    private double totalSaidas;
    private double saldoAtual;
    private String periodo;

    public Resumo(String periodo, List<Financa> financas) {
        this.periodo = periodo;
        calcularResumo(financas);
    }


    private void calcularResumo(List<Financa> financas) {
        double entradas = 0.0;
        double saidas = 0.0;

        for (Financa financa : financas) {
            if (financa.getValor() > 0) {
                entradas += financa.getValor();
            } else {
                saidas += financa.getValor();
            }
        }

        this.totalEntradas = entradas;
        this.totalSaidas = saidas;
        this.saldoAtual = entradas + saidas;
    }

    public double getTotalEntradas() {
        return totalEntradas;
    }

    public double getTotalSaidas() {
        return totalSaidas;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public String getPeriodo() {
        return periodo;
    }
}

