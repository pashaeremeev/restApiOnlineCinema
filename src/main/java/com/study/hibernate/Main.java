package com.study.hibernate;

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


public class Main {

    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        UserDao userDao = new UserDao();
        MovieDao movieDao = new MovieDao();
        server.createContext("/users/", new UserHandler(userDao));
        server.createContext("/movies/", new MovieHandler(movieDao));
        server.createContext("/movies/fav/", new FavMoviesHandler(new FavMoviesDao(userDao, movieDao)));
        server.createContext("/marks/", new MarkMoviesHandler(new MarkMoviesDao(userDao, movieDao)));
        server.setExecutor(null);
        server.start();
    }
}
