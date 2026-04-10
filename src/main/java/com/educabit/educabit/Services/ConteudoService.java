package com.educabit.educabit.Services;

import com.educabit.educabit.Enums.ContentStatus;
import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Model.Conteudo;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.ConteudoRepository;
import com.educabit.educabit.dtos.ContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConteudoService {

    @Autowired
    private ConteudoRepository conteudoRepository;

    public Conteudo CriarConteudo(ContentDTO dto, Usuario author) {
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
