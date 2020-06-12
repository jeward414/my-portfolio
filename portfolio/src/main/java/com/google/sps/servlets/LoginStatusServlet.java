package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.sps.data.LoginStatus;


@WebServlet("/loginStatus")
public class LoginStatusServlet extends HttpServlet {

    private static final UserService userService = UserServiceFactory.getUserService();
    private static final Gson gson = new Gson();
    private static final String COMMENT_HTML = "/comments.html";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        response.setContentType("application/json");
        String goToHome = COMMENT_HTML;
        LoginStatus loginStatus;

        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();

            loginStatus = new LoginStatus(true, userEmail, goToHome);            

        } else {
            loginStatus = new LoginStatus(false, null, goToHome);
        }

        response.getWriter().println(gson.toJson(loginStatus));
    }

}