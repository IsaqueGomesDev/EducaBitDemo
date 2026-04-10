package com.educabit.educabit.services;

import com.educabit.educabit.enums.ContentStatus;
import com.educabit.educabit.enums.Role;
import com.educabit.educabit.model.Conteudo;
import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.ConteudoRepository;
import com.educabit.educabit.dtos.ContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConteudoService {

    @Autowired
    private ConteudoRepository conteudoRepository;

    public Conteudo criarConteudo(ContentDTO dto, Usuario author) {
        Conteudo conteudo = new Conteudo();
        conteudo.setTitle(dto.title());
        conteudo.setBody(dto.body());
        conteudo.setPublic(dto.isPublic());
        conteudo.setAuthor(author);

        // Auto-approve if ADMIN, otherwise PENDING
        if (author.getRole() == Role.ADMIN) {
            conteudo.setStatus(ContentStatus.APPROVED);
        } else {
            conteudo.setStatus(ContentStatus.PENDING);
        }

        return conteudoRepository.save(conteudo);
    }
}


