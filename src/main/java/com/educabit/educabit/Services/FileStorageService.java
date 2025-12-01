package com.educabit.educabit.Services;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    // Extensões permitidas (whitelist)
    private static final String[] EXTENSOES_PERMITIDAS = {
        ".pdf", ".doc", ".docx", ".txt", ".jpg", ".jpeg", ".png", ".gif"
    };
    
    // Tamanho máximo do arquivo (10MB)
    private static final long TAMANHO_MAXIMO = 10 * 1024 * 1024;

    public String salvarArquivo(MultipartFile arquivo) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            return null;
        }

        // Validar tamanho do arquivo
        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new IOException("Arquivo excede o tamanho máximo permitido de 100MB");
        }

        // Validar extensão do arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        if (nomeOriginal == null || nomeOriginal.isEmpty()) {
            throw new IOException("Nome do arquivo inválido");
        }
        
        String extensao = "";
        if (nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf(".")).toLowerCase();
        }
        
        // Verificar se a extensão está na whitelist
        boolean extensaoValida = false;
        for (String ext : EXTENSOES_PERMITIDAS) {
            if (ext.equals(extensao)) {
                extensaoValida = true;
                break;
            }
        }
        
        if (!extensaoValida) {
            throw new IOException("Tipo de arquivo não permitido. Extensões permitidas: " + 
                String.join(", ", EXTENSOES_PERMITIDAS));
        }

        // Criar diretório se não existir
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Gerar nome único para o arquivo (usar apenas UUID para segurança)
        String nomeArquivo = UUID.randomUUID().toString() + extensao;

        // Salvar arquivo
        Path caminhoArquivo = uploadPath.resolve(nomeArquivo).normalize();
        
        // Garantir que o caminho final está dentro do diretório base
        if (!caminhoArquivo.startsWith(uploadPath)) {
            throw new SecurityException("Tentativa de salvar arquivo fora do diretório permitido");
        }
        
        Files.copy(arquivo.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);

        // Retornar caminho relativo
        return uploadDir + "/" + nomeArquivo;
    }

    public Path carregarArquivo(String nomeArquivo) throws IOException {
        if (nomeArquivo == null || nomeArquivo.isEmpty()) {
            throw new IllegalArgumentException("Caminho do arquivo não pode ser nulo ou vazio");
        }
        
        // Normalizar e validar caminho para prevenir Path Traversal
        Path basePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = Paths.get(nomeArquivo).normalize();
        
        // Garantir que o arquivo está dentro do diretório base
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Tentativa de acesso a arquivo fora do diretório permitido");
        }
        
        if (!Files.exists(filePath)) {
            throw new IOException("Arquivo não encontrado: " + nomeArquivo);
        }
        
        return filePath;
    }

    public void deletarArquivo(String caminhoArquivo) throws IOException {
        if (caminhoArquivo == null || caminhoArquivo.isEmpty()) {
            return;
        }
        
        // Normalizar e validar caminho para prevenir Path Traversal
        Path basePath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = Paths.get(caminhoArquivo).normalize();
        
        // Garantir que o arquivo está dentro do diretório base
        if (!filePath.startsWith(basePath)) {
            throw new SecurityException("Tentativa de acesso a arquivo fora do diretório permitido");
        }
        
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}

