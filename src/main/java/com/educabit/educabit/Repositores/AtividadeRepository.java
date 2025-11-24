package com.educabit.educabit.Repositores;

import com.educabit.educabit.Model.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {
}
