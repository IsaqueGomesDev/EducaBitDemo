package com.educabit.educabit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo_video", unique = true)
    private String tituloVideo;

    @Column(name = "descricao_video")
    private String descricaoVideo;

    @Column(name = "pilar_pc")
    private String pilar_pc;

    public Video() {
    }

    public Video(Integer id, String tituloVideo, String descricaoVideo, String pilar_pc) {
        this.id = id;
        this.tituloVideo = tituloVideo;
        this.descricaoVideo = descricaoVideo;
        this.pilar_pc = pilar_pc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTituloVideo() {
        return tituloVideo;
    }

    public void setTituloVideo(String tituloVideo) {
        this.tituloVideo = tituloVideo;
    }

    public String getDescricaoVideo() {
        return descricaoVideo;
    }

    public void setDescricaoVideo(String descricaoVideo) {
        this.descricaoVideo = descricaoVideo;
    }

    public String getPilar_pc() {
        return pilar_pc;
    }

    public void setPilar_pc(String pilar_pc) {
        this.pilar_pc = pilar_pc;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", tituloVideo='" + tituloVideo + '\'' +
                ", descricaoVideo='" + descricaoVideo + '\'' +
                ", pilar_pc='" + pilar_pc + '\'' +
                '}';
    }
}

