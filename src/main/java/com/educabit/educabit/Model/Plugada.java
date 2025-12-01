package com.educabit.educabit.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "plugada")
public class Plugada extends Atividade {

    public Plugada() {
        super();
        this.setTipoAtividade(TipoAtividade.PLUGADA);
    }

    public Plugada(String tituloAtividade, String descricaoAtividade, boolean possuiAcessibilidade, TipoAcessibilidade tipoAcessibilidade, String anexarAtividade) {
        super();
        this.setTipoAtividade(TipoAtividade.PLUGADA);
        this.setTituloAtividade(tituloAtividade);
        this.setDescricaoAtividade(descricaoAtividade);
        this.setPossuiAcessibilidade(possuiAcessibilidade);
        this.setTipoAcessibilidade(tipoAcessibilidade);
        this.setAnexarAtividade(anexarAtividade);
    }
}
