package com.study.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "marks_of_movies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MarksMovies {

    @Id
    @Column(name = "id_note")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "id_movie")
    private Movie movie;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "mark")
    private Integer mark;

    public boolean isNull() {
        return (this.user == null && this.movie == null && this.mark == null);
    }
}
