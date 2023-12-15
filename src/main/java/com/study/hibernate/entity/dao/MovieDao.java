package com.study.hibernate.entity.dao;

import com.study.hibernate.HibernateUtil;
import com.study.hibernate.entity.Movie;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class MovieDao {

    private Session session;

    public List<Movie> getAllMovies() {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> movieRoot = cq.from(Movie.class);
            CriteriaQuery all = cq.select(movieRoot);
            TypedQuery<Movie> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public Movie getMovieById(Integer id) {
        Session session = null;
        Movie movie = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            movie = session.get(Movie.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
        return movie;
    }

    public void save(Movie movie) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.persist(movie);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public void update(Movie movie) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            session.merge(movie);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public Movie getMovieByNames(String nameRu, String nameOrig) {
        Session session = null;
        Movie movie;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            TypedQuery<Movie> query = session.createQuery(
                    "SELECT movie " +
                            "FROM Movie movie "  +
                            "WHERE movie.nameRu = :nameRu and movie.nameOriginal = :nameOrig",
                    Movie.class);
            query.setParameter("nameOrig", nameOrig);
            query.setParameter("nameRu", nameRu);
            movie =  query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
        return movie;
    }

    public Movie getMovieByNameAndYear(String nameRu, Integer year) {
        Session session = null;
        Movie movie;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            TypedQuery<Movie> query = session.createQuery(
                    "SELECT movie " +
                            "FROM Movie movie "  +
                            "WHERE movie.nameRu = :nameRu and movie.year = :year",
                    Movie.class);
            query.setParameter("year", year);
            query.setParameter("nameRu", nameRu);
            movie =  query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.getTransaction().commit();
                session.close();
            }
        }
        return movie;
    }

    public void saveAll(List<Movie> movies) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            for (int i = 0 ; i < movies.size(); i++) {
                session.persist(movies.get(i));
                if (i % 100 == 0) {
                    session.flush();
                    session.clear();
                }
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
}
