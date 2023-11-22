package com.study.hibernate.entity;

import com.study.hibernate.json.MovieJson;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<FavMovies> favMovies;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<MarksMovies> marksMovies;

    public boolean isNull() {
        return (this.nameRu == null && this.countries == null && this.genres == null && this.year == null
        && this.posterUrl == null && this.posterUrlPreview == null && this.stream == null && this.desc == null);
    }

    public MovieJson toMovieJson() {
        MovieJson movieJson = new MovieJson();
        movieJson.setIdMovie(this.idMovie);
        movieJson.setNameRu(this.nameRu);
        movieJson.setNameOriginal(this.nameOriginal);
        movieJson.setCountries(this.countries);
        movieJson.setGenres(this.genres);
        movieJson.setYear(this.year);
        movieJson.setPosterUrl(this.posterUrl);
        movieJson.setPosterUrlPreview(this.posterUrlPreview);
        movieJson.setDesc(this.desc);
        movieJson.setDuration(this.duration);
        movieJson.setRating(this.rating);
        return movieJson;
    }
}
