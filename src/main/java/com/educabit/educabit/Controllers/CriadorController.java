package com.educabit.educabit.Controllers;

import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import com.educabit.educabit.Services.RegistroCriadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/educabit/criador")
public class CriadorController {

    @Autowired
    private RegistroCriadorService registroCriadorService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping(value = "/solicitar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> SolicitarAcessoCriador(
            @RequestParam(value = "lattesUrl", required = false) String lattes,
            @RequestParam("linkedinUrl") String linkedin,
            @RequestParam("documentType") String documentType,
            @RequestParam("documentFile") MultipartFile documentFile,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "phone", required = false) String phone,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário deve estar logado.");
        }

        Usuario usuario = usuarioRepository.findByUsername(principal.getName());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        if (linkedin == null || linkedin.isBlank()) {
            return ResponseEntity.badRequest().body("O Link do LinkedIn é obrigatório.");
        }

        try {
            registroCriadorService.EnviarDocumentos(Objects.requireNonNull(usuario.getId()), lattes,
                    linkedin, documentFile, documentType, bio, phone);
            return ResponseEntity.ok("Documentos enviados com sucesso. Status atualizado para EM ANÁLISE.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar documentos: " + e.getMessage());
        }
    }
}
