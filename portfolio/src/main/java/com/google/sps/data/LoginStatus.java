package com.google.sps.data;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public final class LoginStatus {

    private static final UserService userService = UserServiceFactory.getUserService();

    private final boolean loggedIn;
    private final String user;
    private final String redirect;

    public LoginStatus(boolean loggedIn, String user, String redirect) {
        this.loggedIn = loggedIn;
        this.user = user;
        if (this.loggedIn) {
            this.redirect = userService.createLogoutURL(redirect);
        } else {
            this.redirect = userService.createLoginURL(redirect);
        }
    }

}