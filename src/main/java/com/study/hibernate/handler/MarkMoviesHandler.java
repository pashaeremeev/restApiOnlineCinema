package com.study.hibernate.handler;

import com.google.gson.Gson;
import com.study.hibernate.entity.MarksMovies;
import com.study.hibernate.entity.Movie;
import com.study.hibernate.entity.dao.MarkMoviesDao;
import com.study.hibernate.json.FavMoviesJson;
import com.study.hibernate.json.MarkMoviesJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MarkMoviesHandler implements HttpHandler {

    private MarkMoviesDao markMoviesDao;

    public MarkMoviesHandler(MarkMoviesDao markMoviesDao) {
        this.markMoviesDao = markMoviesDao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String responseText = "";
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            if (path.matches("/marks/\\d+/\\d+$")) {
                String dataText = path.substring(path.lastIndexOf('/') + 1);
                String[] dataIdsText = dataText.split("/");
                Integer movieId = Integer.parseInt(dataIdsText[0]);
                Integer userId = Integer.parseInt(dataIdsText[1]);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                MarksMovies mark = markMoviesDao.getMarkByUserId(movieId, userId);
                if (mark != null) {
                    responseText = new Gson().toJson(mark);
                    exchange.sendResponseHeaders(200, responseText.getBytes(StandardCharsets.UTF_8).length);
                } else {
                    responseText = new Gson().toJson("Mark not found");
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
            if (path.matches("/marks/")) {
                MarkMoviesJson mark = new Gson().fromJson(requestText.toString(), MarkMoviesJson.class);
                if (!mark.isNull()) {
                    if (markMoviesDao.saveOrDelete(mark)) {
                        markMoviesDao.calculateAvgMark(mark.getIdMovie());
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
