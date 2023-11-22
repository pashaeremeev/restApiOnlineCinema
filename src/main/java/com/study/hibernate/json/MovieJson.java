package com.study.hibernate.json;

public class MovieJson {

    private Integer idMovie;

    private String nameRu;

    private String nameOriginal;

    private String[] countries;

    private String[] genres;

    private Integer year;

    private String posterUrl;

    private String posterUrlPreview;

    private String stream;

    private String desc;

    private String duration;

    private Double rating;

    public MovieJson() {

    }

    public MovieJson(Integer idMovie, String nameRu, String nameOriginal, String[] countries, String[] genres,
                     Integer year, String posterUrl, String posterUrlPreview, String stream, String desc,
                     String duration, Double rating) {
        this.idMovie = idMovie;
        this.nameRu = nameRu;
        this.nameOriginal = nameOriginal;
        this.countries = countries;
        this.genres = genres;
        this.year = year;
        this.posterUrl = posterUrl;
        this.posterUrlPreview = posterUrlPreview;
        this.stream = stream;
        this.desc = desc;
        this.duration = duration;
        this.rating = rating;
    }

    public Integer getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(Integer idMovie) {
        this.idMovie = idMovie;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameOriginal() {
        return nameOriginal;
    }

    public void setNameOriginal(String nameOriginal) {
        this.nameOriginal = nameOriginal;
    }

    public String[] getCountries() {
        return countries;
    }

    public void setCountries(String[] countries) {
        this.countries = countries;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getPosterUrlPreview() {
        return posterUrlPreview;
    }

    public void setPosterUrlPreview(String posterUrlPreview) {
        this.posterUrlPreview = posterUrlPreview;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
