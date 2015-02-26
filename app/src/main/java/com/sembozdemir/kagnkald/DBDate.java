package com.sembozdemir.kagnkald;

import java.io.Serializable;

/**
 * Created by Semih Bozdemir on 23.2.2015.
 */
public class DBDate implements Serializable {
    private static final long serialVersionUID = 1L;

    // Database fields
    private int id;
    private String date;
    private String description;

    public DBDate() {
        super();
    }

    public DBDate(String date, String description) {
        super();
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
