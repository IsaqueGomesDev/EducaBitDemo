package com.educabit.educabit.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "tipoAcessibilidade")
public class TipoAcessibilidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTipoAcessibilidade;
    private String nome;
    private String descricao;

    public TipoAcessibilidade() {
    }

    public TipoAcessibilidade(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getIdTipoAcessibilidade() {
        return idTipoAcessibilidade;
    }

    public void setIdTipoAcessibilidade(int idTipoAcessibilidade) {
        this.idTipoAcessibilidade = idTipoAcessibilidade;
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
}

