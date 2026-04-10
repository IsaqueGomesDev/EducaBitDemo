package com.educabit.educabit.controllers;

import com.educabit.educabit.enums.UserStatus;
import com.educabit.educabit.model.Usuario;
import com.educabit.educabit.repositories.UsuarioRepository;
import com.educabit.educabit.services.RegistroCriadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RegistroCriadorService registroCriadorService;

    @GetMapping("/criadores/pendentes")
    public ResponseEntity<List<Usuario>> listarSolicitacoesPendentes() {
        return ResponseEntity.ok(usuarioRepository.findByStatus(UserStatus.IN_REVIEW));
    }

    @PostMapping("/criadores/aprovar")
    public ResponseEntity<?> aprovarCriador(@RequestBody Map<String, Object> payload) {
        int userId = Integer.parseInt(payload.get("userId").toString());
        boolean approved = Boolean.parseBoolean(payload.get("approved").toString());
        String reason = (String) payload.get("reason");

        try {
            registroCriadorService.aprovarCriador(Objects.requireNonNull(userId), approved, reason);
            return ResponseEntity.ok("SolicitaÃ§Ã£o " + (approved ? "aprovada" : "rejeitada") + " com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar solicitaÃ§Ã£o: " + e.getMessage());
        }
    }
}


