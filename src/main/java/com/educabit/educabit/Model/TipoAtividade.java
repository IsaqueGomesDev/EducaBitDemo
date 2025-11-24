package com.educabit.educabit.Model;

public enum TipoAtividade {
    PLUGADA("Plugada"),
    DESPLUGADA("Desplugada");

    private final String valor;

    TipoAtividade(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}

