package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/chart-data")
public class ChartDataServlet extends HttpServlet {

    private Map<String, Integer> gameVotes = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().println(gson.toJson(gameVotes));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String game = request.getParameter("game");
        int currentVotes = gameVotes.containsKey(game) ? gameVotes.get(game) : 0;
        gameVotes.put(game, currentVotes + 1);

        response.sendRedirect("/charts.html");
    }
}