package com.study.hibernate;

import com.study.hibernate.entity.FavMovies;
import com.study.hibernate.entity.MarksMovies;
import com.study.hibernate.entity.dao.FavMoviesDao;
import com.study.hibernate.entity.dao.MarkMoviesDao;
import com.study.hibernate.entity.dao.MovieDao;
import com.study.hibernate.entity.dao.UserDao;
import com.study.hibernate.handler.FavMoviesHandler;
import com.study.hibernate.handler.MarkMoviesHandler;
import com.study.hibernate.handler.MovieHandler;
import com.study.hibernate.handler.UserHandler;
import com.sun.net.httpserver.HttpServer;


import java.io.IOException;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        UserDao userDao = new UserDao();
        MovieDao movieDao = new MovieDao();
        FavMoviesDao favMoviesDao = new FavMoviesDao(userDao, movieDao);
        MarkMoviesDao markMoviesDao = new MarkMoviesDao(userDao, movieDao);
        server.createContext("/users/", new UserHandler(userDao, markMoviesDao));
        server.createContext("/movies/", new MovieHandler(movieDao));
        server.createContext("/movies/fav/", new FavMoviesHandler(favMoviesDao, userDao));
        server.createContext("/marks/", new MarkMoviesHandler(markMoviesDao, userDao));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }
}
