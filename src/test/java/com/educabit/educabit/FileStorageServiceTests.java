package com.educabit.educabit;

import com.educabit.educabit.services.FileStorageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class FileStorageServiceTests {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("Deve salvar arquivo PDF com nome UUID")
    void salvarArquivoPdf() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "documento.pdf", "application/pdf",
                "conteÃºdo do PDF".getBytes());

        String resultado = fileStorageService.salvarArquivo(file);

        assertNotNull(resultado);
        assertTrue(resultado.endsWith(".pdf"), "Caminho deveria terminar com .pdf");
    }

    @Test
    @DisplayName("Deve salvar arquivo JPG")
    void salvarArquivoJpg() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "foto.jpg", "image/jpeg",
                "fake image content".getBytes());

        String resultado = fileStorageService.salvarArquivo(file);

        assertNotNull(resultado);
        assertTrue(resultado.endsWith(".jpg"), "Caminho deveria terminar com .jpg");
    }

    @Test
    @DisplayName("Deve rejeitar extensÃ£o nÃ£o permitida (.exe)")
    void rejeitarExtensaoNaoPermitida() {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "malware.exe", "application/octet-stream",
                "hack content".getBytes());

        IOException exception = assertThrows(IOException.class,
                () -> fileStorageService.salvarArquivo(file));

        assertTrue(exception.getMessage().contains("Tipo de arquivo nÃ£o permitido"));
    }

    @Test
    @DisplayName("Deve rejeitar extensÃ£o .bat")
    void rejeitarExtensaoBat() {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "script.bat", "application/octet-stream",
                "Malicious content".getBytes());

        assertThrows(IOException.class, () -> fileStorageService.salvarArquivo(file));
    }

    @Test
    @DisplayName("Deve retornar null para arquivo vazio")
    void retornarNullParaArquivoVazio() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "vazio.pdf", "application/pdf",
                new byte[0]);

        String resultado = fileStorageService.salvarArquivo(file);
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve retornar null para arquivo null")
    void retornarNullParaNull() throws IOException {
        String resultado = fileStorageService.salvarArquivo(null);
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve carregar arquivo salvo anteriormente")
    void carregarArquivoSalvo() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "testload.txt", "text/plain",
                "texto de teste".getBytes());

        String nomeArquivo = fileStorageService.salvarArquivo(file);
        assertNotNull(nomeArquivo);

        // Extract just the filename from the full path
        String fileName = Path.of(nomeArquivo).getFileName().toString();
        Path resultado = fileStorageService.carregarArquivo(fileName);

        assertNotNull(resultado);
        assertTrue(resultado.toFile().exists(), "Arquivo deveria existir no disco");
    }

    @Test
    @DisplayName("Deve lanÃ§ar exceÃ§Ã£o ao carregar arquivo inexistente")
    void carregarArquivoInexistente() {
        assertThrows(IOException.class,
                () -> fileStorageService.carregarArquivo("arquivo_que_nao_existe.pdf"));
    }

    @Test
    @DisplayName("Deve deletar arquivo existente sem exceÃ§Ã£o")
    void deletarArquivoExistente() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "arquivo", "testdelete.txt", "text/plain",
                "conteÃºdo para deletar".getBytes());

        String nomeArquivo = fileStorageService.salvarArquivo(file);
        assertNotNull(nomeArquivo);

        // NÃ£o deve lanÃ§ar exceÃ§Ã£o
        String fileName = Path.of(nomeArquivo).getFileName().toString();
        assertDoesNotThrow(() -> fileStorageService.deletarArquivo(fileName));
    }

    @Test
    @DisplayName("Deve rejeitar nome de arquivo invÃ¡lido (null)")
    void rejeitarNomeArquivoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> fileStorageService.carregarArquivo(null));
    }

    @Test
    @DisplayName("Deletar arquivo nulo deve ser silencioso")
    void deletarArquivoNulo() throws IOException {
        // NÃ£o deve lanÃ§ar exceÃ§Ã£o
        assertDoesNotThrow(() -> fileStorageService.deletarArquivo(null));
        assertDoesNotThrow(() -> fileStorageService.deletarArquivo(""));
    }
}

