package com.educabit.educabit.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoDto(
    @JsonProperty("tituloVideo") String tituloVideo,
    @JsonProperty("descricaoVideo") String descricaoVideo,
    @JsonProperty("pilar_pc") String pilar_pc
) {}
