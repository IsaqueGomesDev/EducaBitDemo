CREATE TABLE desplugada (
    idAtividade INTEGER PRIMARY KEY DEFAULT nextval('atividade_seq'),
    tipoAtividade VARCHAR(255) NOT NULL DEFAULT 'DESPLUGADA',
    tituloAtividade VARCHAR(255) NOT NULL,
    descricaoAtividade TEXT,
    possuiAcessibilidade BOOLEAN DEFAULT FALSE,
    idTipoAcessibilidade INTEGER,
    anexarAtividade VARCHAR(255),
    CONSTRAINT fk_tipoAcessibilidade_desplugada FOREIGN KEY (idTipoAcessibilidade) REFERENCES tipoAcessibilidade(idTipoAcessibilidade)
);

