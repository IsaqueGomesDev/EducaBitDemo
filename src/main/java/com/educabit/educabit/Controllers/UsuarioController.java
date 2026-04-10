package com.educabit.educabit.Controllers;

import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import com.educabit.educabit.dtos.UsuarioDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/educabit/usuario")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> AdicionarUsuario(@RequestBody @NonNull UsuarioDto usuario) {
        try {
            logger.info("Criando usuário: {}, CPF: {}", usuario.username(), usuario.cpf());
            var usuario1 = new Usuario();
            usuario1.setUsername(usuario.username());
            usuario1.setEmail(usuario.email());
            usuario1.setPassword(usuario.password());
            usuario1.setCpf(usuario.cpf());

            // Definir type com valor default se não fornecido (coluna NOT NULL no banco)
            if (usuario.type() != null && !usuario.type().isBlank()) {
                usuario1.setType(usuario.type());
            } else {
                usuario1.setType("USER");
            }

            // Set default status
            usuario1.setStatus(UserStatus.PENDING);

            // Force Role.USER for all new public registrations
            usuario1.setRole(Role.USER);

            // Log if they tried to be a creator
            if (usuario.type() != null && usuario.type().equalsIgnoreCase("CREATOR")) {
                logger.info("User attempted to register as CREATOR. Defaulting to USER.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario1));
        } catch (Exception e) {
            logger.error("Erro ao criar usuário", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> BuscarPeloId(@PathVariable(value = "id") @NonNull Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuario.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> RemoverUsuario(@PathVariable(value = "id") @NonNull Integer Id) {
        Optional<Usuario> usuario = usuarioRepository.findById(Id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        usuarioRepository.delete(usuario.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario removido com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> AtualizarUsuario(@PathVariable(value = "id") @NonNull Integer Id,
            @RequestBody @NonNull UsuarioDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findById(Id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        var usuarioModel = usuario.get();

        if (dto.username() != null && !dto.username().isBlank())
            usuarioModel.setUsername(dto.username());
        if (dto.type() != null && !dto.type().isBlank())
            usuarioModel.setType(dto.type());
        if (dto.email() != null && !dto.email().isBlank())
            usuarioModel.setEmail(dto.email());
        if (dto.password() != null && !dto.password().isBlank())
            usuarioModel.setPassword(dto.password());
        if (dto.cpf() != null && !dto.cpf().isBlank())
            usuarioModel.setCpf(dto.cpf());

        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuarioModel));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> ListarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> BuscarPorCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(usuarioRepository.findByCpfContaining(cpf));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }
        Usuario usuario = usuarioRepository.findByUsername(principal.getName());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        return ResponseEntity.ok(usuario);
    }
}
