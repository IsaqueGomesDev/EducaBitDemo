package com.educabit.educabit.controllers;

import com.educabit.educabit.model.Conteudo;
import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.ConteudoRepository;
import com.educabit.educabit.repositories.UsuarioRepository;
import com.educabit.educabit.services.ConteudoService;
import com.educabit.educabit.dtos.ContentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/educabit/conteudo")
public class ConteudoController {

    @Autowired
    private ConteudoRepository conteudoRepository;

    @Autowired
    private ConteudoService conteudoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/publico")
    public ResponseEntity<Page<Conteudo>> listarConteudoPublico(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(conteudoRepository.findForAnonymous(pageable));
    }

    @GetMapping
    public ResponseEntity<Page<Conteudo>> listarConteudo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(conteudoRepository.findForSubscriber(pageable));
    }

    @PostMapping
    public ResponseEntity<?> criarConteudo(@RequestBody ContentDTO dto, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UsuÃ¡rio deve estar logado");
        }
        Usuario author = usuarioRepository.findByUsername(principal.getName());
        if (author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UsuÃ¡rio nÃ£o encontrado");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(conteudoService.criarConteudo(dto, author));
    }
}


