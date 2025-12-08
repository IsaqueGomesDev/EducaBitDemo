CREATE TABLE tipoAcessibilidade (
    idTipoAcessibilidade SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT
);

-- Inserir tipos de acessibilidade iniciais
INSERT INTO tipoAcessibilidade (nome, descricao) VALUES 
('Deficiência Visual', 'Atividades adaptadas para pessoas com deficiência visual'),
('Deficiência Auditiva', 'Atividades adaptadas para pessoas com deficiência auditiva'),
('Deficiência Motora', 'Atividades adaptadas para pessoas com deficiência motora'),
('Deficiência Intelectual', 'Atividades adaptadas para pessoas com deficiência intelectual'),
('Múltipla Deficiência', 'Atividades adaptadas para pessoas com múltiplas deficiências'),
('Nenhuma', 'Atividade sem necessidade de acessibilidade específica');

