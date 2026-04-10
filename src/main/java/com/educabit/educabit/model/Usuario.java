package com.educabit.educabit.model;

import com.educabit.educabit.enums.Role;
import com.educabit.educabit.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "usuario")
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username; // Keeping username as login identifier per existing code

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Legacy field, mapped to Role now
    private String type;

    // --- Controle de Acesso (RBAC) ---
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, CREATOR, USER

    // --- MÃ¡quina de Estados do UsuÃ¡rio ---
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    // --- Dados de ValidaÃ§Ã£o do Criador (KYC) ---
    private String documentType; // RG, CNH, CTPS
    private String documentUrl; // Foto do documento

    @Deprecated
    private String cpf; // Mantido para compatibilidade, mas nÃ£o usado no novo fluxo

    private String phone; // Para 2FA/MFA

    private String lattesUrl; // Link do currÃ­culo

    private String diplomaUrl; // Link do S3/MinIO onde estÃ¡ o PDF

    private String linkedinUrl; // Perfil do LinkedIn

    @Column(length = 500)
    private String bio; // Pequena biografia

    // --- Auditoria JurÃ­dica ---
    private boolean termsAccepted; // O checkbox "Aceito os termos"
    private LocalDateTime termsAcceptedAt; // Quando aceitou
    private String registrationIp; // IP de quem aceitou

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
}

