package com.study.hibernate.entity.dao;

import com.study.hibernate.HibernateUtil;
import com.study.hibernate.entity.MarksMovies;
import com.study.hibernate.entity.Movie;
import com.study.hibernate.entity.User;
import com.study.hibernate.json.MarkMoviesJson;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class MarkMoviesDao {

    private Session session;
    private UserDao userDao;
    private MovieDao movieDao;

    public MarkMoviesDao(UserDao userDao, MovieDao movieDao) {
        this.userDao = userDao;
        this.movieDao = movieDao;
    }

    public void calculateAvgMark(Integer idMovie) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            Movie movie = movieDao.getMovieById(idMovie);
            TypedQuery<Double> query = session.createQuery(
                    "SELECT AVG(mark) " +
                            "FROM MarksMovies marksMovies " +
                            "WHERE marksMovies.movie.idMovie = :movieId",
                    Double.class);
            query.setParameter("movieId", idMovie);
            Double avgMark = query.getSingleResult();
            Double result = Math.floor(avgMark * 100) / 100;
            if (movie != null) {
                movie.setRating(result);
                movieDao.update(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public Integer getMarkByUserId(Integer idMovie, Integer idUser) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            TypedQuery<Integer> query = session.createQuery(
                    "SELECT mark " +
                            "FROM MarksMovies markMovie " +
                            //"INNER JOIN Movie movie ON movie.idMovie = markMovie.movie.idMovie " +
                            "WHERE markMovie.user.idUser = :userId AND markMovie.movie.idMovie = :movieId",
                    Integer.class);
            query.setParameter("userId", idUser);
            query.setParameter("movieId", idMovie);
            return query.getSingleResult();
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public List<Integer> getMoviesIdsOfUsersMarks(Integer idUser) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            TypedQuery<Integer> query = session.createQuery(
                    "SELECT markMovie.movie.idMovie " +
                            "FROM MarksMovies markMovie " +
                            "WHERE markMovie.user.idUser = :userId",
                    Integer.class);
            query.setParameter("userId", idUser);
            return query.getResultList();
        } catch (Exception e) {
            //e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public boolean saveOrUpdate(MarkMoviesJson json) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            User user = userDao.getUserById(json.getIdUser());
            Movie movie = movieDao.getMovieById(json.getIdMovie());
            if (user != null && movie != null) {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<MarksMovies> query = builder.createQuery(MarksMovies.class);
                Root<MarksMovies> root = query.from(MarksMovies.class);
                query.select(root);
                query.where(
                        builder.equal(root.get("user"), user),
                        builder.equal(root.get("movie"), movie)
                );
                MarksMovies note = session.createQuery(query).uniqueResult();
                if (note != null) {
                    note.setMark(json.getMark());
                    session.merge(note);
                } else {
                    MarksMovies newNote = new MarksMovies();
                    newNote.setMovie(movie);
                    newNote.setUser(user);
                    newNote.setMark(json.getMark());
                    session.persist(newNote);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
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
