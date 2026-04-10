package com.educabit.educabit.Controllers;

import com.educabit.educabit.Model.Atividade;
import com.educabit.educabit.Model.TipoAtividade;
import com.educabit.educabit.Model.TipoAcessibilidade;
import com.educabit.educabit.Model.Plugada;
import com.educabit.educabit.Model.Desplugada;
import com.educabit.educabit.Repositores.AtividadeRepository;
import com.educabit.educabit.Repositores.TipoAcessibilidadeRepository;
import com.educabit.educabit.Services.FileStorageService;
import com.educabit.educabit.dtos.AtividadeDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/educabit/atividade")
public class AtividadeController {

    private static final Logger logger = LoggerFactory.getLogger(AtividadeController.class);

    @Autowired
    AtividadeRepository atividadeRepository;

    @Autowired
    TipoAcessibilidadeRepository tipoAcessibilidadeRepository;

    @Autowired
    FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<Atividade>> ListarAtividades() {
        return ResponseEntity.status(HttpStatus.OK).body(atividadeRepository.findAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> AdicionarAtividade(
            @RequestPart("atividade") AtividadeDto atividade,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
        try {
            // Validar tipoAtividade obrigatório
            if (atividade.tipoAtividade() == null || atividade.tipoAtividade().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Tipo de atividade é obrigatório");
            }

            logger.debug("DTO Recebido: {}", atividade);
            logger.debug("Titulo: {}", atividade.tituloAtividade());
            logger.debug("Tipo: {}", atividade.tipoAtividade());

            TipoAtividade tipoAtividade = converterStringParaEnum(atividade.tipoAtividade());
            if (tipoAtividade == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Tipo de atividade inválido. Valores aceitos: PLUGADA ou DESPLUGADA");
            }

            TipoAcessibilidade tipoAcessibilidade = null;

            // Buscar tipo de acessibilidade pelo ID se fornecido
            if (atividade.tipoAcessibilidade() != null && !atividade.tipoAcessibilidade().isEmpty()) {
                try {
                    Integer idTipoAcessibilidade = Integer.parseInt(atividade.tipoAcessibilidade());
                    Optional<TipoAcessibilidade> tipoAcessibilidadeOpt = tipoAcessibilidadeRepository
                            .findById(idTipoAcessibilidade);
                    if (tipoAcessibilidadeOpt.isPresent()) {
                        tipoAcessibilidade = tipoAcessibilidadeOpt.get();
                    }
                } catch (NumberFormatException e) {
                    // Se não for um número, ignora
                }
            }

            Atividade atividade1;

            // Criar instância da subclasse correta baseado no tipo
            if (tipoAtividade == TipoAtividade.PLUGADA) {
                atividade1 = new Plugada(
                        atividade.tituloAtividade(),
                        atividade.descricaoAtividade(),
                        atividade.possuiAcessibilidade(),
                        tipoAcessibilidade,
                        null);
            } else {
                atividade1 = new Desplugada(
                        atividade.tituloAtividade(),
                        atividade.descricaoAtividade(),
                        atividade.possuiAcessibilidade(),
                        tipoAcessibilidade,
                        null);
            }

            // Salvar arquivo se fornecido
            if (arquivo != null && !arquivo.isEmpty()) {
                String caminhoArquivo = fileStorageService.salvarArquivo(arquivo);
                atividade1.setAnexarAtividade(caminhoArquivo);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(atividadeRepository.save(atividade1));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao salvar atividade", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar atividade: " + e.toString());
        }
    }

    @GetMapping("/{idAtividade}")
    public ResponseEntity<?> BuscarPeloIdAtividade(@PathVariable(value = "idAtividade") @NonNull Integer idAtividade) {
        Optional<Atividade> atividade = atividadeRepository.findById(idAtividade);
        if (atividade.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(atividade.get());
    }

    @DeleteMapping("/{idAtividade}")
    public ResponseEntity<String> RemoverAtividade(@PathVariable(value = "idAtividade") @NonNull Integer idAtividade) {
        try {
            Optional<Atividade> atividade = atividadeRepository.findById(idAtividade);
            if (atividade.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada");
            }

            // Deletar arquivo associado se existir
            Atividade atividadeModel = atividade.get();
            String caminhoArquivo = atividadeModel.getAnexarAtividade();
            if (caminhoArquivo != null && !caminhoArquivo.isEmpty()) {
                fileStorageService.deletarArquivo(caminhoArquivo);
            }

            atividadeRepository.delete(atividadeModel);
            return ResponseEntity.status(HttpStatus.OK).body("Atividade removida com sucesso");
        } catch (Exception e) {
            logger.error("Erro ao remover atividade", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover atividade. Por favor, tente novamente.");
        }
    }

    @PutMapping(value = "/{idAtividade}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> AtualizarAtividade(
            @PathVariable(value = "idAtividade") @NonNull Integer idAtividade,
            @RequestPart("atividade") AtividadeDto dto,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
        try {
            Optional<Atividade> atividade = atividadeRepository.findById(idAtividade);
            if (atividade.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada");
            }
            var atividadeModel = atividade.get();

            // Deletar arquivo antigo se existir e um novo arquivo for fornecido
            if (arquivo != null && !arquivo.isEmpty() && atividadeModel.getAnexarAtividade() != null) {
                fileStorageService.deletarArquivo(atividadeModel.getAnexarAtividade());
            }

            atividadeModel.setTituloAtividade(dto.tituloAtividade());
            atividadeModel.setDescricaoAtividade(dto.descricaoAtividade());
            atividadeModel.setPossuiAcessibilidade(dto.possuiAcessibilidade());
            atividadeModel.setTipoAtividade(converterStringParaEnum(dto.tipoAtividade()));

            // Buscar tipo de acessibilidade pelo ID se fornecido
            if (dto.tipoAcessibilidade() != null && !dto.tipoAcessibilidade().isEmpty()) {
                try {
                    Integer idTipoAcessibilidade = Integer.parseInt(dto.tipoAcessibilidade());
                    Optional<TipoAcessibilidade> tipoAcessibilidadeOpt = tipoAcessibilidadeRepository
                            .findById(idTipoAcessibilidade);
                    if (tipoAcessibilidadeOpt.isPresent()) {
                        atividadeModel.setTipoAcessibilidade(tipoAcessibilidadeOpt.get());
                    }
                } catch (NumberFormatException e) {
                    // Se não for um número, mantém o valor atual
                }
            }

            // Salvar novo arquivo se fornecido
            if (arquivo != null && !arquivo.isEmpty()) {
                String caminhoArquivo = fileStorageService.salvarArquivo(arquivo);
                atividadeModel.setAnexarAtividade(caminhoArquivo);
            }

            return ResponseEntity.status(HttpStatus.OK).body(atividadeRepository.save(atividadeModel));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar atividade", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar atividade. Por favor, tente novamente.");
        }
    }

    @GetMapping("/{idAtividade}/arquivo")
    public ResponseEntity<?> DownloadArquivo(@PathVariable(value = "idAtividade") @NonNull Integer idAtividade) {
        try {
            Optional<Atividade> atividade = atividadeRepository.findById(idAtividade);
            if (atividade.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Atividade não encontrada");
            }

            String caminhoArquivo = atividade.get().getAnexarAtividade();
            if (caminhoArquivo == null || caminhoArquivo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não encontrado para esta atividade");
            }

            Path arquivo = fileStorageService.carregarArquivo(caminhoArquivo);
            Resource resource = new UrlResource(arquivo.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não pode ser lido");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acesso negado ao arquivo.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Arquivo não encontrado.");
        } catch (Exception e) {
            logger.error("Erro ao baixar arquivo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao baixar arquivo. Por favor, tente novamente.");
        }
    }

    private TipoAtividade converterStringParaEnum(String tipoAtividade) {
        if (tipoAtividade == null) {
            return null;
        }
        try {
            return TipoAtividade.valueOf(tipoAtividade.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Se não encontrar o enum, tenta buscar pelo valor
            for (TipoAtividade tipo : TipoAtividade.values()) {
                if (tipo.getValor().equalsIgnoreCase(tipoAtividade)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException(
                    "Tipo de atividade inválido: " + tipoAtividade + ". Valores aceitos: PLUGADA ou DESPLUGADA");
        }
    }

}
