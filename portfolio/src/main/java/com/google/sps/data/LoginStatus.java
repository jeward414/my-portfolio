package com.google.sps.data;

public final class LoginStatus {

    private final boolean loggedIn;
    private final String user;
    private final String redirect;

    public LoginStatus(boolean loggedIn, String user, String redirect) {
        this.loggedIn = loggedIn;
        this.user = user;
        this.redirect = redirect;
    }

}