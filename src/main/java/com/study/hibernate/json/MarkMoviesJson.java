package com.study.hibernate.json;

public class MarkMoviesJson {

    private Integer idMovie;

    private Integer idUser;

    private Integer mark;

    public MarkMoviesJson(Integer idMovie, Integer idUser, Integer mark) {
        this.idMovie = idMovie;
        this.idUser = idUser;
        this.mark = mark;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public Integer getMark() {
        return mark;
    }

    public boolean isNull() {
        return (this.idUser == null && this.idMovie == null && this.mark == null);
    }
}
