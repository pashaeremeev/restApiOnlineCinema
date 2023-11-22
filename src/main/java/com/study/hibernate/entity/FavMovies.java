package com.study.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fav_movies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FavMovies {

    @Id
    @Column(name = "id_note")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "id_movie")
    private Movie movie;

    public boolean isNull() {
        return (this.user == null && this.movie == null);
    }
}
