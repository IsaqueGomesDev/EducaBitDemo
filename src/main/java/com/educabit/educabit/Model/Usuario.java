package com.educabit.educabit.Model;

<<<<<<< Updated upstream

public class Usuario {

    private int id;
    private String name;
=======
import com.educabit.educabit.Enums.Role;
import com.educabit.educabit.Enums.UserStatus;
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

    // --- Máquina de Estados do Usuário ---
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    // --- Dados de Validação do Criador (KYC) ---
    private String documentType; // RG, CNH, CTPS
    private String documentUrl; // Foto do documento

    @Deprecated
    private String cpf; // Mantido para compatibilidade, mas não usado no novo fluxo

    private String phone; // Para 2FA/MFA

    private String lattesUrl; // Link do currículo

    private String diplomaUrl; // Link do S3/MinIO onde está o PDF

    private String linkedinUrl; // Perfil do LinkedIn

    @Column(length = 500)
    private String bio; // Pequena biografia

    // --- Auditoria Jurídica ---
    private boolean termsAccepted; // O checkbox "Aceito os termos"
    private LocalDateTime termsAcceptedAt; // Quando aceitou
    private String registrationIp; // IP de quem aceitou

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
>>>>>>> Stashed changes
}
