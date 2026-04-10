package com.educabit.educabit.repositories;

import com.educabit.educabit.model.TipoAcessibilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoAcessibilidadeRepository extends JpaRepository<TipoAcessibilidade, Integer> {
}



