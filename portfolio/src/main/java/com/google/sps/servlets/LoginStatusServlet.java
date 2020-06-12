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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        response.setContentType("application/json");
        
        LoginStatus loginStatus;

        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            String goToHome = "index.html";

            loginStatus = new LoginStatus(true, userEmail, goToHome);            

        } else {
            String goToHome = "index.html";
            loginStatus = new LoginStatus(false, null, goToHome);
        }

        response.getWriter().println(gson.toJson(loginStatus));
    }

}