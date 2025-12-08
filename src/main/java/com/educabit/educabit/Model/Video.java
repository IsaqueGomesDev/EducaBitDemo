package com.educabit.educabit.Model;

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
    private String tituloVideo;

    @Column(name = "descricao_video")
    private String descricaoVideo;

    @Column(name = "pilar_pc")
    private String pilar_pc;

}
