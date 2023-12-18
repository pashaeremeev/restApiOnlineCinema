package com.study.hibernate.handler;

import com.google.gson.Gson;
import com.study.hibernate.entity.User;
import com.study.hibernate.entity.dao.MarkMoviesDao;
import com.study.hibernate.entity.dao.UserDao;
import com.study.hibernate.json.UserJson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements HttpHandler {

    private UserDao userDao;
    private MarkMoviesDao markMoviesDao;

    public UserHandler(UserDao userDao, MarkMoviesDao markMoviesDao) {
        this.userDao = userDao;
        this.markMoviesDao = markMoviesDao;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String responseText = "";
        String userData = path.substring(path.lastIndexOf('/') + 1);
            // Обработка GET-запроса на эндпоинт /users/{id}
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            if (path.matches("/users/\\d+$")) {
                Integer userId = Integer.parseInt(userData);
                User user = userDao.getUserById(userId); //получение пользователя по айди
                // Отправка ответа
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (user != null) {
                    responseText = new Gson().toJson(user.toUserJson());
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("User not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
                // Обработка GET-запроса на эндпоинт /users/{username}
            } else if (path.matches("/users/[a-zA-Z0-9]+[._ -]?[a-zA-Z0-9]*$")) {
                User user = userDao.getUserByUsername(userData); //получение пользователя по имени
                // Отправка ответа
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (user != null) {
                    responseText = new Gson().toJson(user.toUserJson());
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("User not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
                // Обработка GET-запроса на эндпоинт /users/
            } else if (path.matches("/users/")) {
                List<User> users = userDao.getAllUsers(); //получение списка пользователей
                // Отправка ответа
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (!users.isEmpty()) {
                    List<UserJson> userJsons = new ArrayList<>();
                    for (int i = 0; i < users.size(); i++) {
                        UserJson userJson = users.get(i).toUserJson();
                        userJsons.add(userJson);
                    }
                    responseText = new Gson().toJson(userJsons);
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("Users not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
            } else {
                responseText = new Gson().toJson("Method not allowed");
                exchange.sendResponseHeaders(405, responseText.getBytes().length);
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream reader = exchange.getRequestBody();
            BufferedReader buf = new BufferedReader(new InputStreamReader(reader));
            StringBuffer requestText = new StringBuffer();
            String line;
            while ((line = buf.readLine()) != null) {
                requestText.append(line);
            }
            buf.close();
            if (path.matches("/users/")) {
                User user = new Gson().fromJson(requestText.toString(), User.class);
                User userIsExists = userDao.getUserByUsername(user.getUsername());
                if (userIsExists == null) {
                    userDao.save(user);
                    if (user.getIdUser() != null) {
                        responseText = new Gson().toJson(user.getIdUser() + "");
                        exchange.sendResponseHeaders(201, responseText.getBytes().length);
                    } else {
                        responseText = new Gson().toJson("Bad Request: check post data");
                        exchange.sendResponseHeaders(400, responseText.getBytes().length);
                    }
                } else {
                    responseText = new Gson().toJson("User with this username is exist");
                    exchange.sendResponseHeaders(403, responseText.getBytes().length);
                }
            } else {
                responseText = new Gson().toJson("Method not allowed");
                exchange.sendResponseHeaders(405, responseText.getBytes().length);
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("PUT")) {
            /*InputStream reader = exchange.getRequestBody();
            BufferedReader buf = new BufferedReader(new InputStreamReader(reader));
            StringBuffer requestText = new StringBuffer();
            String line;
            while ((line = buf.readLine()) != null) {
                requestText.append(line);
            }
            buf.close();*/
            if (path.matches("/users/block/\\d+$")) {
                Integer userId = Integer.parseInt(userData);
                User user = userDao.getUserById(userId);
                if (user != null) {
                    userDao.block(userId);
                    responseText = new Gson().toJson("User is blocked");
                    exchange.sendResponseHeaders(202, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("User not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
            } else if (path.matches("/users/unblock/\\d+$")) {
                Integer userId = Integer.parseInt(userData);
                User user = userDao.getUserById(userId);
                if (user != null) {
                    userDao.unblock(userId);
                    responseText = new Gson().toJson("User is unblocked");
                    exchange.sendResponseHeaders(202, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("User not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
            } else if (path.matches("/users/appoint/\\d+$")) {
                Integer userId = Integer.parseInt(userData);
                User user = userDao.getUserById(userId);
                if (user != null) {
                    userDao.appointAdm(userId);
                    responseText = new Gson().toJson("User is admin");
                    exchange.sendResponseHeaders(202, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("User not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
            } else {
                responseText = new Gson().toJson("Method not allowed");
                exchange.sendResponseHeaders(405, responseText.getBytes().length);
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
            if (path.matches("/users/\\d+$")) {
                Integer userId = Integer.parseInt(userData);
                User user = userDao.getUserById(userId); //получение пользователя по айди
                // Отправка ответа
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (user != null) {
                    List<Integer> moviesIds = markMoviesDao.getMoviesIdsOfUsersMarks(userId);
                    userDao.deleteById(userId);
                    if (!moviesIds.isEmpty()) {
                        for (int i = 0; i < moviesIds.size(); i++) {
                            Integer movieId = moviesIds.get(i);
                            markMoviesDao.calculateAvgMark(movieId);
                        }
                    }
                    responseText = new Gson().toJson("Deleted");
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                } else {
                    responseText = new Gson().toJson("User not found");
                    exchange.sendResponseHeaders(404, responseText.getBytes().length);
                }
            }
        }

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseText.getBytes());
        outputStream.close();

    }
}
