package com.educabit.educabit.Repositores;

import com.educabit.educabit.Model.TipoAcessibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAcessibilidadeRepository extends JpaRepository<TipoAcessibilidade, Integer> {
}

