package com.educabit.educabit.Repositores;

import com.educabit.educabit.Model.Conteudo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {

        // 1. For Anonymous Users: Only Public & Approved
        @Query("SELECT c FROM Conteudo c WHERE c.status = 'APPROVED' AND c.isPublic = true")
        Page<Conteudo> findForAnonymous(Pageable pageable);

        // 2. For Subscribers (Logged in): All Approved
        @Query("SELECT c FROM Conteudo c WHERE c.status = 'APPROVED'")
        Page<Conteudo> findForSubscriber(Pageable pageable);

        // 3. For Creator (My Contents): All statuses, filtered by Author ID
        @Query("SELECT c FROM Conteudo c WHERE c.author.id = :authorId")
        Page<Conteudo> findMyCreations(@Param("authorId") Integer authorId, Pageable pageable);

        // 4. For Admin (Moderation): Pending Approval
        @Query("SELECT c FROM Conteudo c WHERE c.status = 'PENDING'")
        Page<Conteudo> findPendingApproval(Pageable pageable);
}
