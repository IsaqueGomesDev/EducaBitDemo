-- Seed Admin User
-- Password 'admin' (This should be BCrypt encoded in production, but preserving dev default 'admin')
INSERT INTO usuario (username, type, email, password, role, status, cpf)
VALUES ('admin', 'ADMIN', 'admin@educabit.com', 'admin', 'ADMIN', 'ACTIVE', '12345678900')
ON CONFLICT (email) DO NOTHING;
