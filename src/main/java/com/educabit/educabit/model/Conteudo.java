package com.educabit.educabit.model;

import com.educabit.educabit.enums.ContentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conteudo") // Renamed table to match Portuguese convention
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties("password")
    private Usuario author;

    @Enumerated(EnumType.STRING)
    private ContentStatus status;

    private boolean isPublic;
}

