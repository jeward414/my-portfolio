package com.google.sps.data;

import java.util.Date;

public final class Comment {

    private String name;
    private String comment;
    private String email;
    private Date date;

    public Comment() {
        this.name = "";
        this.comment = "";
        this.email = "";
        this.date = null;
    }

    public Comment(String name, String comment, String email, Date date) {
        this.name = name;
        this.comment = comment;
        this.email = email;
        this.date = date;
    }

}