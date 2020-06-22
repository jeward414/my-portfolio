package com.google.sps.data;

import java.util.Date;

public final class Comment {

    private String name;
    private String text;
    private String email;
    private Date date;

    public Comment() {
        this.name = "";
        this.text = "";
        this.email = "";
        this.date = null;
    }

    public Comment(String name, String comment, String email, Date date) {
        this.name = name;
        this.text = comment;
        this.email = email;
        this.date = date;
    }

}