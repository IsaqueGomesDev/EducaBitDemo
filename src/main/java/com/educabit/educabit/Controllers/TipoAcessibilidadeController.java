package com.educabit.educabit.Controllers;

import com.educabit.educabit.Model.TipoAcessibilidade;
import com.educabit.educabit.Repositores.TipoAcessibilidadeRepository;
import com.educabit.educabit.dtos.TipoAcessibilidadeDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/educabit/tipoAcessibilidade")
public class TipoAcessibilidadeController {

    @Autowired
    TipoAcessibilidadeRepository tipoAcessibilidadeRepository;

    @PostMapping
    public ResponseEntity<TipoAcessibilidade> AdicionarTipoAcessibilidade(@RequestBody TipoAcessibilidadeDto tipoAcessibilidade) {
        var tipoAcessibilidade1 = new TipoAcessibilidade();
        BeanUtils.copyProperties(tipoAcessibilidade, tipoAcessibilidade1);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoAcessibilidadeRepository.save(tipoAcessibilidade1));
    }

    @GetMapping
    public ResponseEntity<?> ListarTodosTiposAcessibilidade() {
        return ResponseEntity.status(HttpStatus.OK).body(tipoAcessibilidadeRepository.findAll());
    }

    @GetMapping("/{idTipoAcessibilidade}")
    public ResponseEntity<?> BuscarPeloIdTipoAcessibilidade(@PathVariable(value="idTipoAcessibilidade") Integer idTipoAcessibilidade) {
        Optional<TipoAcessibilidade> tipoAcessibilidade = tipoAcessibilidadeRepository.findById(idTipoAcessibilidade);
        if(!tipoAcessibilidade.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de acessibilidade não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(tipoAcessibilidade.get());
    }

    @DeleteMapping("/{idTipoAcessibilidade}")
    public ResponseEntity<String> RemoverTipoAcessibilidade(@PathVariable(value="idTipoAcessibilidade") Integer idTipoAcessibilidade) {
        Optional<TipoAcessibilidade> tipoAcessibilidade = tipoAcessibilidadeRepository.findById(idTipoAcessibilidade);
        if(!tipoAcessibilidade.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de acessibilidade não encontrado");
        }
        tipoAcessibilidadeRepository.delete(tipoAcessibilidade.get());
        return ResponseEntity.status(HttpStatus.OK).body("Tipo de acessibilidade removido com sucesso");
    }

    @PutMapping("/{idTipoAcessibilidade}")
    public ResponseEntity<?> AtualizarTipoAcessibilidade(@PathVariable(value="idTipoAcessibilidade") Integer idTipoAcessibilidade, @RequestBody TipoAcessibilidadeDto dto) {
        Optional<TipoAcessibilidade> tipoAcessibilidade = tipoAcessibilidadeRepository.findById(idTipoAcessibilidade);
        if(!tipoAcessibilidade.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de acessibilidade não encontrado");
        }
        var tipoAcessibilidadeModel = tipoAcessibilidade.get();
        BeanUtils.copyProperties(dto, tipoAcessibilidadeModel);
        return ResponseEntity.status(HttpStatus.OK).body(tipoAcessibilidadeRepository.save(tipoAcessibilidadeModel));
    }
}

