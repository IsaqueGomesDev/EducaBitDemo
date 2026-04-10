package com.educabit.educabit.Services;

import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
import com.educabit.educabit.Model.Usuario;
import com.educabit.educabit.Repositores.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class RegistroCriadorService {

    @Autowired
    private UsuarioRepository userRepo;

    @Autowired
    private FileStorageService fileStorageService;

    // Passo 1: Usuário envia os documentos e informações
    public void EnviarDocumentos(@NonNull Integer userId, String lattes,
            String linkedin, MultipartFile documentFile, String documentType,
            String bio, String phone) throws IOException {
        Usuario user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setLattesUrl(lattes);

        if (linkedin != null && !linkedin.isBlank()) {
            user.setLinkedinUrl(linkedin);
        }

        if (documentFile != null && !documentFile.isEmpty()) {
            String caminhoArquivo = fileStorageService.salvarArquivo(documentFile);
            user.setDocumentUrl(caminhoArquivo);
        }

        if (documentType != null && !documentType.isBlank()) {
            user.setDocumentType(documentType);
        }

        if (bio != null && !bio.isBlank()) {
            user.setBio(bio);
        }

        if (phone != null && !phone.isBlank()) {
            user.setPhone(phone);
        }

        // Muda estado para "EM ANÁLISE"
        user.setStatus(UserStatus.IN_REVIEW);

        userRepo.save(user);
    }

    // Passo 2: Admin aprova (Endpoint exclusivo de Admin)
    public void AprovarCriador(@NonNull Integer userId, boolean approved, String reason) {
        Usuario user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (approved) {
            user.setStatus(UserStatus.ACTIVE);
            user.setRole(Role.CREATOR); // Fix #7: Promover role para CREATOR ao aprovar
        } else {
            user.setStatus(UserStatus.BLOCKED);
        }

        userRepo.save(user);
    }
}
