package com.google.sps.data;

import java.util.Date;

public final class Comment {

    private String name;
    private String comment;
    private Date date;

    public Comment() {
        this.name = "";
        this.comment = "";
        this.date = null;
    }

    public Comment(String name, String comment, Date date) {
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

}