package com.study.hibernate.json;

public class UserJson {

    private Integer idUser;

    private String username;

    private String password;

    private Boolean isActive;

    private Integer idRole;

    public UserJson() {

    }

    public UserJson(Integer idUser, String username, String password, Boolean isActive, Integer idRole) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.idRole = idRole;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
}
