package com.study.hibernate.entity.dao;

import com.study.hibernate.HibernateUtil;
import com.study.hibernate.entity.FavMovies;
import com.study.hibernate.entity.Movie;
import com.study.hibernate.entity.User;
import com.study.hibernate.json.FavMoviesJson;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class FavMoviesDao {

    private Session session;
    private UserDao userDao;
    private MovieDao movieDao;

    public FavMoviesDao(UserDao userDao, MovieDao movieDao) {
        this.userDao = userDao;
        this.movieDao = movieDao;
    }

    public List<Movie> getMoviesById(Integer idUser) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            TypedQuery<Movie> query = session.createQuery(
                    "SELECT movie " +
                            "FROM Movie movie " +
                            "INNER JOIN FavMovies favMovies ON movie = favMovies.movie " +
                            "WHERE favMovies.user.idUser = :userId",
                    Movie.class);
            query.setParameter("userId", idUser);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public boolean saveOrDelete(FavMoviesJson json) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            User user = userDao.getUserById(json.getIdUser());
            Movie movie = movieDao.getMovieById(json.getIdMovie());
            if (user != null && movie != null) {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<FavMovies> query = builder.createQuery(FavMovies.class);
                Root<FavMovies> root = query.from(FavMovies.class);
                query.select(root);
                query.where(
                        builder.equal(root.get("user"), user),
                        builder.equal(root.get("movie"), movie)
                );
                FavMovies note = session.createQuery(query).uniqueResult();
                if (note != null) {
                    session.remove(note);
                } else {
                    FavMovies newNote = new FavMovies();
                    newNote.setUser(user);
                    newNote.setMovie(movie);
                    session.persist(newNote);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
        return true;
    }
}
