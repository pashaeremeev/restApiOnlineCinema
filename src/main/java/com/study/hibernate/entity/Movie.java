package com.study.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CollectionType;

import java.util.List;


@Entity
@Table(name = "movies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @Column(name = "id_movie")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovie;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_orig")
    private String nameOriginal;

    @Column(name = "countries")
    @CollectionType(type = com.study.hibernate.converter.ListStringType.class)
    private String[] countries;

    @Column(name = "genres")
    @CollectionType(type = com.study.hibernate.converter.ListStringType.class)
    private String[] genres;

    @Column(name = "year")
    private Integer year;

    @Column(name = "poster")
    private String posterUrl;

    @Column(name = "preview")
    private String posterUrlPreview;

    @Column(name = "stream")
    private String stream;

    @Column(name = "description")
    private String desc;

    @Column(name = "duration")
    private String duration;

    @Column(name = "rating")
    private Double rating;

    public boolean isNull() {
        return (this.nameRu == null && this.countries == null && this.genres == null && this.year == null
        && this.posterUrl == null && this.posterUrlPreview == null && this.stream == null && this.desc == null);
    }
}
