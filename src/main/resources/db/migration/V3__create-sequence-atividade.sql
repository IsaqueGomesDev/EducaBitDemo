-- Criar sequência compartilhada para as tabelas plugada e desplugada
-- Esta sequência será usada por ambas as tabelas para garantir IDs únicos
CREATE SEQUENCE IF NOT EXISTS atividade_seq START WITH 1 INCREMENT BY 1;

