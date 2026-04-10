package com.educabit.educabit.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AtividadeDto(
        @JsonProperty("tipoAtividade") String tipoAtividade,
        @JsonProperty("tituloAtividade") String tituloAtividade,
        @JsonProperty("descricaoAtividade") String descricaoAtividade,
        @JsonProperty("possuiAcessibilidade") boolean possuiAcessibilidade,
        @JsonProperty("tipoAcessibilidade") String tipoAcessibilidade,
        @JsonProperty("anexarAtividade") String anexarAtividade) {
}
