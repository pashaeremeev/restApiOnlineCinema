package com.study.hibernate.entity.dao;

import com.study.hibernate.HibernateUtil;
import com.study.hibernate.entity.FavMovies;
import com.study.hibernate.entity.MarksMovies;
import com.study.hibernate.entity.Movie;
import com.study.hibernate.entity.User;
import com.study.hibernate.json.MarkMoviesJson;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

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
                            "WHERE marksMovies.movie = :movie",
                    Double.class);
            query.setParameter("movie", movie);
            Double avgMark = query.getSingleResult();
            if (movie != null) {
                movie.setRating(avgMark);
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

    public MarksMovies getMarkByUserId(Integer idMovie, Integer idUser) {
        MarksMovies note = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            User user = userDao.getUserById(idMovie);
            Movie movie = movieDao.getMovieById(idUser);
            if (user != null && movie != null) {
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<MarksMovies> query = builder.createQuery(MarksMovies.class);
                Root<MarksMovies> root = query.from(MarksMovies.class);
                query.select(root);
                query.where(
                        builder.equal(root.get("user"), user),
                        builder.equal(root.get("movie"), movie)
                );
                note = session.createQuery(query).uniqueResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
        return note;
    }

    public boolean saveOrDelete(MarkMoviesJson json) {
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
