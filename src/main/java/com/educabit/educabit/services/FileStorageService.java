package com.educabit.educabit.services;

import com.educabit.educabit.config.FileStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "NÃ£o foi possÃ­vel criar o diretÃ³rio onde os arquivos enviados serÃ£o armazenados.", ex);
        }
    }

    // ExtensÃµes permitidas (whitelist)
    private static final String[] EXTENSOES_PERMITIDAS = {
            ".pdf", ".doc", ".docx", ".txt", ".jpg", ".jpeg", ".png", ".gif"
    };

    // Tamanho mÃ¡ximo do arquivo (100MB)
    private static final long TAMANHO_MAXIMO = 100 * 1024 * 1024;

    public String salvarArquivo(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        // Validar tamanho do arquivo
        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new IOException("Arquivo excede o tamanho mÃ¡ximo permitido de 100MB");
        }

        // Validar extensÃ£o do arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || nomeOriginal.isEmpty()) {
            throw new IOException("Nome do arquivo invÃ¡lido");
        }

        String extensao = "";
        if (nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf(".")).toLowerCase();
        }

        // Verificar se a extensÃ£o estÃ¡ na whitelist
        boolean extensaoValida = false;
        for (String ext : EXTENSOES_PERMITIDAS) {
            if (ext.equals(extensao)) {
                extensaoValida = true;
                break;
            }
        }

        if (!extensaoValida) {
            throw new IOException("Tipo de arquivo nÃ£o permitido. ExtensÃµes permitidas: " +
                    String.join(", ", EXTENSOES_PERMITIDAS));
        }

        // O diretÃ³rio jÃ¡ Ã© criado no construtor, podemos usar this.fileStorageLocation
        // Mas mantendo a lÃ³gica de validaÃ§Ã£o do mÃ©todo original:
        Path uploadPath = this.fileStorageLocation;
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Gerar nome Ãºnico para o arquivo (usar apenas UUID para seguranÃ§a)
        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        // Salvar arquivo
        Path caminhoArquivo = uploadPath.resolve(nomeArquivo).normalize();

        // Garantir que o caminho final estÃ¡ dentro do diretÃ³rio base
        if (!caminhoArquivo.startsWith(uploadPath)) {
            throw new SecurityException("Tentativa de salvar arquivo fora do diretÃ³rio permitido");
        }

        Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

        // Retornar caminho relativo
        return fileStorageLocation.toString() + "/" + nomeArquivo;
    }

    public Path carregarArquivo(String nomeArquivo) throws IOException {
        if (nomeArquivo == null || nomeArquivo.isEmpty()) {
            throw new IllegalArgumentException("Caminho do arquivo nÃ£o pode ser nulo ou vazio");
        }

        // Normalizar e validar caminho para prevenir Path Traversal
        Path basePath = this.fileStorageLocation;
        Path filePath = basePath.resolve(nomeArquivo).normalize();

        // Garantir que o arquivo estÃ¡ dentro do diretÃ³rio base
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Tentativa de acesso a arquivo fora do diretÃ³rio permitido");
        }

        if (!Files.exists(filePath)) {
            throw new IOException("Arquivo nÃ£o encontrado: " + nomeArquivo);
        }

        return filePath;
    }

    public void deletarArquivo(String caminhoArquivo) throws IOException {
        if (caminhoArquivo == null || caminhoArquivo.isEmpty()) {
            return;
        }

        // Normalizar e validar caminho para prevenir Path Traversal
        Path basePath = this.fileStorageLocation;
        Path filePath = basePath.resolve(caminhoArquivo).normalize();

        // Garantir que o arquivo estÃ¡ dentro do diretÃ³rio base
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Tentativa de acesso a arquivo fora do diretÃ³rio permitido");
        }

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}

