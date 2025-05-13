package com.financeiro.financeiro.model;

public class Conta {

    private static int contador = 1;
    private int id;
    private String nome;
    private double saldo;
    private User user;

    public Conta(String nome, double saldo, User user) {
        this.id = contador++;
        this.nome = nome;
        this.saldo = saldo;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
