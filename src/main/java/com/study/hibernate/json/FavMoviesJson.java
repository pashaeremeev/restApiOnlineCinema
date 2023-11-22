package com.study.hibernate.json;

public class FavMoviesJson {

    private Integer idUser;

    private Integer idMovie;

    public FavMoviesJson(Integer idUser, Integer idMovie) {
        this.idUser = idUser;
        this.idMovie = idMovie;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public boolean isNull() {
        return (this.idUser == null && this.idMovie == null);
    }
}
