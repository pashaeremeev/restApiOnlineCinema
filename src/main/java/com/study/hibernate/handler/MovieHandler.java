package com.study.hibernate.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.study.hibernate.entity.Movie;

import com.study.hibernate.entity.dao.MovieDao;

import com.study.hibernate.json.MovieJson;
import com.study.hibernate.json.UserJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieHandler implements HttpHandler {

    private MovieDao movieDao;

    public MovieHandler(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String responseText = "";
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            if (path.matches("/movies/")) {
                List<Movie> allMovies = movieDao.getAllMovies();
                List<MovieJson> movieJsons = new ArrayList<>();
                for (int i = 0; i < allMovies.size(); i++) {
                    MovieJson movieJson = allMovies.get(i).toMovieJson();
                    movieJsons.add(movieJson);
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                responseText = new Gson().toJson(movieJsons);
                exchange.sendResponseHeaders(200, responseText.getBytes(StandardCharsets.UTF_8).length);
            } else if (path.matches("/movies/\\d+$")) {
                String movieIdText = path.substring(path.lastIndexOf('/') + 1);
                Integer movieId = Integer.parseInt(movieIdText);
                Movie movie = movieDao.getMovieById(movieId);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (movie != null) {
                    responseText = new Gson().toJson(movie.toMovieJson());
                    exchange.sendResponseHeaders(200, responseText.getBytes(StandardCharsets.UTF_8).length);
                } else {
                    responseText = new Gson().toJson("Movie not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes(StandardCharsets.UTF_8).length);
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
            if (path.matches("/movies/")) {
                Type typeOfList = new TypeToken<ArrayList<Movie>>(){}.getType();
                ArrayList<Movie> movies = new Gson().fromJson(requestText.toString(), typeOfList);
                //Movie movie = new Gson().fromJson(requestText.toString(), Movie.class);
                //movie.setRating(0.0);
                if (movies != null || !movies.isEmpty()) {
                    for (int i = 0; i < movies.size(); i++) {
                        Movie movie = movies.get(i);
                        Movie oldMovie;
                        if (movie.getNameOriginal() == null) {
                            oldMovie = movieDao.getMovieByNameAndYear(movie.getNameRu(), movie.getYear());
                        } else {
                            oldMovie = movieDao.getMovieByNames(movie.getNameRu(), movie.getNameOriginal());
                        }
                        if (oldMovie != null) {
                            oldMovie.update(movie);
                            movieDao.update(oldMovie);
                        } else {
                            movie.setRating(0.0);
                            movieDao.save(movie);
                        }
                    }
                    responseText = new Gson().toJson("Created");
                    exchange.sendResponseHeaders(201, responseText.getBytes(StandardCharsets.UTF_8).length);
                } else {
                    responseText = new Gson().toJson("Bad Request: check post data");
                    exchange.sendResponseHeaders(400, responseText.getBytes(StandardCharsets.UTF_8).length);
                }
                /*if (!movie.isNull()) {
                    movieDao.save(movie);
                    responseText = new Gson().toJson(movie.getIdMovie() + "");
                    exchange.sendResponseHeaders(201, responseText.getBytes(StandardCharsets.UTF_8).length);
                } else {
                    responseText = new Gson().toJson("Bad Request: check post data");
                    exchange.sendResponseHeaders(400, responseText.getBytes(StandardCharsets.UTF_8).length);
                }*/
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
        outputStream.close();
    }
}