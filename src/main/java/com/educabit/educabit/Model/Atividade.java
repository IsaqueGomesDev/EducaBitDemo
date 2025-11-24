package com.educabit.educabit.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.SequenceGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SequenceGenerator(name = "atividade_seq", sequenceName = "atividade_seq", allocationSize = 1)
public abstract class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "atividade_seq")
    private int idAtividade;
    @Enumerated(EnumType.STRING)
    private TipoAtividade tipoAtividade;
    private String tituloAtividade;
    private String descricaoAtividade;
    private boolean possuiAcessibilidade;
    @ManyToOne
    @JoinColumn(name = "idTipoAcessibilidade")
    private TipoAcessibilidade tipoAcessibilidade;
    private String anexarAtividade;

    public Atividade(){

    }

    public Atividade(int idAtividade, TipoAtividade tipoAtividade, String tituloAtividade, String descricaoAtividade, boolean possuiAcessibilidade, TipoAcessibilidade tipoAcessibilidade, String anexarAtividade) {
        this.idAtividade = idAtividade;
        this.tipoAtividade = tipoAtividade;
        this.tituloAtividade = tituloAtividade;
        this.descricaoAtividade = descricaoAtividade;
        this.possuiAcessibilidade = possuiAcessibilidade;
        this.tipoAcessibilidade = tipoAcessibilidade;
        this.anexarAtividade = anexarAtividade;
    }



    public int getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(int idAtividade) {
        this.idAtividade = idAtividade;
    }

    public TipoAtividade getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividade tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public String getTituloAtividade() {
        return tituloAtividade;
    }

    public void setTituloAtividade(String tituloAtividade) {
        this.tituloAtividade = tituloAtividade;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public boolean isPossuiAcessibilidade() {
        return possuiAcessibilidade;
    }

    public void setPossuiAcessibilidade(boolean possuiAcessibilidade) {
        this.possuiAcessibilidade = possuiAcessibilidade;
    }

    public TipoAcessibilidade getTipoAcessibilidade() {
        return tipoAcessibilidade;
    }

    public void setTipoAcessibilidade(TipoAcessibilidade tipoAcessibilidade) {
        this.tipoAcessibilidade = tipoAcessibilidade;
    }

    public String getAnexarAtividade() {
        return anexarAtividade;
    }

    public void setAnexarAtividade(String anexarAtividade) {
        this.anexarAtividade = anexarAtividade;
    }
}
