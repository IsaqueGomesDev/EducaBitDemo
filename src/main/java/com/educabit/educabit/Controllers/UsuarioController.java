package com.educabit.educabit.Controllers;

import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import com.educabit.educabit.dtos.UsuarioDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/educabit/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity AdicionarUsuario(@RequestBody UsuarioDto usuario){
        var usuario1 = new Usuario();
        BeanUtils.copyProperties(usuario,usuario1);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario1));

    }

    @GetMapping("/{Id}")
    public ResponseEntity BuscarPeloId(@PathVariable(value="id") Integer Id){
        Optional usuario = usuarioRepository.findById(Id);
        if(usuario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(usuario.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity RemoverUsuario(@PathVariable(value="id") Integer Id){
        Optional<Usuario> usuario = usuarioRepository.findById(Id);
        if(usuario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        usuarioRepository.delete(usuario.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario removido com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity AtualizarUsuario(@PathVariable(value="id") Integer Id, @RequestBody UsuarioDto dto){
        Optional<Usuario> usuario = usuarioRepository.findById(Id);
        if(usuario.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        var usuarioModel = usuario.get();
        BeanUtils.copyProperties(dto,usuarioModel);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuarioModel));
    }



}
