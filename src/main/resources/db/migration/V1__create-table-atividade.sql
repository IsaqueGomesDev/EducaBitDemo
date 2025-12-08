CREATE TABLE atividade (
    idAtividade SERIAL PRIMARY KEY,
    tipoAtividade VARCHAR(255) NOT NULL,
    tituloAtividade VARCHAR(255) NOT NULL,
    descricaoAtividade TEXT,
    possuiAcessibilidade BOOLEAN DEFAULT FALSE,
    tipoAcessibilidade VARCHAR(255),
    anexarAtividade VARCHAR(255)
);
