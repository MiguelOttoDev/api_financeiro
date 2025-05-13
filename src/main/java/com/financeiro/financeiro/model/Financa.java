package com.financeiro.financeiro.model;


import java.util.ArrayList;
import java.util.List;


public class Financa {
    private static int contador = 1;
    private int id;
    private String nome;
    private String descricao;
    private TipoFinanca tipo;
    private double valor;
    private String data;
    private List<String> historicoTransacoes;

    public Financa() {}

    public Financa(String nome, String descricao, double valor, String data, TipoFinanca tipo) {
        this.id = contador++;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
        this.historicoTransacoes = new ArrayList<>();
    }
    public void adicionarTransacao(String transacao) {
        historicoTransacoes.add(transacao);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    public void setHistoricoTransacoes(List<String> historicoTransacoes) {
        this.historicoTransacoes = historicoTransacoes;
    }

    public static int getProximoId() {
        return contador++;
    }

    public TipoFinanca getTipo() {
        return tipo;
    }

    public void setTipo(TipoFinanca tipo) {
        this.tipo = tipo;
    }

}
