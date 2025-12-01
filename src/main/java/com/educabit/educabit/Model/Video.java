package com.educabit.demo.infrastructure.entitys;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "video")
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "titulo_video", unique = true)
    private String titulo_video;

    @Column(name = "descricao_video")
    private String descricao_video;

    @Column(name = "pilar_pc")
    private String pilar_pc;

}
