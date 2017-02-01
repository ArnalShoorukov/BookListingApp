package com.example.arnal.booklistingapp;

import java.util.Date;

/**
 * Created by arnal on 1/30/17.
 */

public class Books {
    private String title;
    private String subTitle;
    private String author;
    private String publishedDate;

    public Books(String title, String subTitle, String author, String publishedDate){
        this.title = title;
        this.subTitle = subTitle;
        this.author = author;
        this.publishedDate = publishedDate;
    }


    public String getTitle(){return this.title;}

    public String getSubTitle(){return this.subTitle;}

    public String getAuthor(){return this.author;}

    public String getPubDate(){return this.publishedDate;}
}
