package com.educabit.educabit.dtos;

public record AtividadeDto(
    String tipoAtividade,
    String tituloAtividade,
    String descricaoAtividade,
    boolean possuiAcessibilidade,
    String tipoAcessibilidade,
    String anexarAtividade
) {
}

