package com.study.hibernate.handler;

import com.google.gson.Gson;
import com.study.hibernate.entity.FavMovies;
import com.study.hibernate.entity.Movie;
import com.study.hibernate.entity.User;
import com.study.hibernate.entity.dao.FavMoviesDao;
import com.study.hibernate.entity.dao.UserDao;
import com.study.hibernate.json.FavMoviesJson;
import com.study.hibernate.json.MovieJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FavMoviesHandler implements HttpHandler {

    private FavMoviesDao favMoviesDao;
    private UserDao userDao;

    public FavMoviesHandler(FavMoviesDao favMoviesDao, UserDao userDao) {
        this.favMoviesDao = favMoviesDao;
        this.userDao = userDao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String responseText = "";
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            if (path.matches("/movies/fav/\\d+$")) {
                String userIdText = path.substring(path.lastIndexOf('/') + 1);
                Integer userId = Integer.parseInt(userIdText);
                User user = userDao.getUserById(userId);
                if (user.getIsActive()) {
                    List<Integer> movies = favMoviesDao.getMoviesById(userId);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    if (!movies.isEmpty()) {
                        responseText = new Gson().toJson(movies);
                        exchange.sendResponseHeaders(200, responseText.getBytes(StandardCharsets.UTF_8).length);
                    } else {
                        responseText = new Gson().toJson("Favorite movies not found");
                        exchange.sendResponseHeaders(404, responseText.getBytes(StandardCharsets.UTF_8).length);
                    }
                } else {
                    responseText = new Gson().toJson("User is blocked");
                    exchange.sendResponseHeaders(406, responseText.getBytes(StandardCharsets.UTF_8).length);
                }
            } else {
                responseText = new Gson().toJson("Method not allowed");
                exchange.sendResponseHeaders(405, responseText.getBytes(StandardCharsets.UTF_8).length);
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream reader = exchange.getRequestBody();
            BufferedReader buf = new BufferedReader(new InputStreamReader(reader, "UTF-8"));
            StringBuffer requestText = new StringBuffer();
            String line;
            while ((line = buf.readLine()) != null) {
                requestText.append(line);
            }
            buf.close();
            if (path.matches("/movies/fav/")) {
                FavMoviesJson movie = new Gson().fromJson(requestText.toString(), FavMoviesJson.class);
                User user = userDao.getUserById(movie.getIdUser());
                if (user.getIsActive()) {
                    if (!movie.isNull()) {
                        if (favMoviesDao.saveOrDelete(movie)) {
                            responseText = new Gson().toJson("Information is changed");
                            exchange.sendResponseHeaders(200, responseText.getBytes(StandardCharsets.UTF_8).length);
                        } else {
                            responseText = new Gson().toJson("Bad Request: check post data");
                            exchange.sendResponseHeaders(400, responseText.getBytes(StandardCharsets.UTF_8).length);
                        }
                    } else {
                        responseText = new Gson().toJson("Bad Request: check post data");
                        exchange.sendResponseHeaders(400, responseText.getBytes(StandardCharsets.UTF_8).length);
                    }
                } else {
                    responseText = new Gson().toJson("User is blocked");
                    exchange.sendResponseHeaders(406, responseText.getBytes(StandardCharsets.UTF_8).length);
                }
            } else {
                responseText = new Gson().toJson("Method not allowed");
                exchange.sendResponseHeaders(405, responseText.getBytes(StandardCharsets.UTF_8).length);
            }
        } else {
            responseText = new Gson().toJson("Method not allowed");
            exchange.sendResponseHeaders(405, responseText.getBytes(StandardCharsets.UTF_8).length);
        }

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
