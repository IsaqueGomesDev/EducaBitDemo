package com.educabit.educabit.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "desplugada")
public class Desplugada extends Atividade {

    public Desplugada() {
        super();
        this.setTipoAtividade(TipoAtividade.DESPLUGADA);
    }

    //Construtor Desplugada
    public Desplugada(String tituloAtividade, String descricaoAtividade, boolean possuiAcessibilidade, TipoAcessibilidade tipoAcessibilidade, String anexarAtividade) {
        super();
        this.setTipoAtividade(TipoAtividade.DESPLUGADA);
        this.setTituloAtividade(tituloAtividade);
        this.setDescricaoAtividade(descricaoAtividade);
        this.setPossuiAcessibilidade(possuiAcessibilidade);
        this.setTipoAcessibilidade(tipoAcessibilidade);
        this.setAnexarAtividade(anexarAtividade);
    }
}
