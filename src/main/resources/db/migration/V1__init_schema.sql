-- 1. Tabelas Independentes (Sem Foreign Keys)

-- Tabela Endereco (Base para Usuario)
CREATE TABLE endereco (
    id SERIAL PRIMARY KEY,
    cep VARCHAR(20),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    localidade VARCHAR(255),
    uf VARCHAR(2)
);

-- Tabela TipoAcessibilidade (Base para Atividades)
CREATE TABLE tipoAcessibilidade (
    idTipoAcessibilidade SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT
);

INSERT INTO tipoAcessibilidade (nome, descricao) VALUES 
('Deficiência Visual', 'Atividades adaptadas para pessoas com deficiência visual'),
('Deficiência Auditiva', 'Atividades adaptadas para pessoas com deficiência auditiva'),
('Deficiência Motora', 'Atividades adaptadas para pessoas com deficiência motora'),
('Deficiência Intelectual', 'Atividades adaptadas para pessoas com deficiência intelectual'),
('Múltipla Deficiência', 'Atividades adaptadas para pessoas com múltiplas deficiências'),
('Nenhuma', 'Atividade sem necessidade de acessibilidade específica');

-- Tabela Video (Independente)
CREATE TABLE video (
    id SERIAL PRIMARY KEY,
    titulo_video VARCHAR(255) UNIQUE,
    descricao_video VARCHAR(255),
    pilar_pc VARCHAR(255)
);

-- Tabela Access Logs (Logs de Acesso)
CREATE TABLE access_logs (
    id BIGSERIAL PRIMARY KEY,
    ip_address VARCHAR(255),
    user_agent VARCHAR(255),
    referer VARCHAR(255),
    location VARCHAR(255),
    request_url VARCHAR(255),
    method VARCHAR(50),
    username VARCHAR(255),
    timestamp TIMESTAMP
);

-- 2. Tabela Usuario e Dependências

CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL, -- Legacy
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(255), -- Nullable in new schema (replaced by Document Type)
    
    -- RBAC & Status
    role VARCHAR(50),
    status VARCHAR(50) DEFAULT 'PENDING',
    
    -- Profile & KYC
    phone VARCHAR(20),
    bio VARCHAR(500),
    lattes_url VARCHAR(255),
    diploma_url VARCHAR(255), -- Deprecated but kept for schema compatibility or legacy reasoning
    linkedin_url VARCHAR(255),
    
    -- Document Identification
    document_type VARCHAR(50),
    document_url VARCHAR(255),
    
    -- Legal
    terms_accepted BOOLEAN DEFAULT FALSE,
    terms_accepted_at TIMESTAMP,
    registration_ip VARCHAR(255),
    
    -- Address FK
    endereco_id INTEGER,
    CONSTRAINT fk_usuario_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

-- 3. Conteúdo (Depende de Usuario)
CREATE TABLE conteudo (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    body TEXT,
    author_id INTEGER REFERENCES usuario(id),
    status VARCHAR(50),
    is_public BOOLEAN DEFAULT FALSE
);

-- 4. Atividades (Hierarquia e Herança Lógica)

-- Sequência para IDs de atividades
CREATE SEQUENCE IF NOT EXISTS atividade_seq START WITH 1 INCREMENT BY 1;

-- Tabela Legada de Atividade (Se ainda usada pelo sistema genericamente)
CREATE TABLE atividade (
    idAtividade SERIAL PRIMARY KEY,
    tipoAtividade VARCHAR(255) NOT NULL,
    tituloAtividade VARCHAR(255) NOT NULL,
    descricaoAtividade TEXT,
    possuiAcessibilidade BOOLEAN DEFAULT FALSE,
    tipoAcessibilidade VARCHAR(255),
    anexarAtividade VARCHAR(255)
);

-- Tabela Plugada
CREATE TABLE plugada (
    idAtividade INTEGER PRIMARY KEY DEFAULT nextval('atividade_seq'),
    tipoAtividade VARCHAR(255) NOT NULL DEFAULT 'PLUGADA',
    tituloAtividade VARCHAR(255) NOT NULL,
    descricaoAtividade TEXT,
    possuiAcessibilidade BOOLEAN DEFAULT FALSE,
    idTipoAcessibilidade INTEGER,
    anexarAtividade VARCHAR(255),
    CONSTRAINT fk_tipoAcessibilidade_plugada FOREIGN KEY (idTipoAcessibilidade) REFERENCES tipoAcessibilidade(idTipoAcessibilidade)
);

-- Tabela Desplugada
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
