package com.study.hibernate.json;

public class FavMoviesJson {

    private Integer idUser;

    private Integer idMovie;

    private Boolean isFav;

    public FavMoviesJson(Integer idUser, Integer idMovie, Boolean isFav) {
        this.idUser = idUser;
        this.idMovie = idMovie;
        this.isFav = isFav;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public Boolean getFav() {
        return isFav;
    }

    public boolean isNull() {
        return (this.idUser == null && this.idMovie == null && this.isFav == null);
    }
}
