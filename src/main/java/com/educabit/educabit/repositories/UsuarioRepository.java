package com.educabit.educabit.repositories;

import com.educabit.educabit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByUsernameAndPassword(String username, String password);

    Usuario findByUsername(String username);

    java.util.List<Usuario> findByCpfContaining(String cpf);

    java.util.List<com.educabit.educabit.model.Usuario> findByStatus(com.educabit.educabit.enums.UserStatus status);
}


