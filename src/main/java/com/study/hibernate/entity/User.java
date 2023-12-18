package com.study.hibernate.entity;

import com.study.hibernate.json.UserJson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer idUser;

    @Column(name = "name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "id_role")
    private Integer idRole;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH },
            mappedBy = "user", fetch = FetchType.EAGER)
    private List<FavMovies> favMovies;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<MarksMovies> marksMovies;

    public UserJson toUserJson() {
        UserJson userJson = new UserJson();
        userJson.setIdUser(this.idUser);
        userJson.setUsername(this.username);
        userJson.setPassword(this.password);
        userJson.setActive(this.isActive);
        userJson.setIdRole(this.idRole);
        return userJson;
    }
}
