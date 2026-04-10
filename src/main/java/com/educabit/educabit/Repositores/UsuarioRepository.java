package com.educabit.educabit.Repositores;

import com.educabit.educabit.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByUsernameAndPassword(String username, String password);

    Usuario findByUsername(String username);

    java.util.List<Usuario> findByCpfContaining(String cpf);

    java.util.List<com.educabit.educabit.Model.Usuario> findByStatus(com.educabit.educabit.Enums.UserStatus status);
}
