package com.example.arnal.booklistingapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arnal on 1/31/17.
 */

public class BookAdapter extends ArrayAdapter<Books>{


    public BookAdapter(Activity context, ArrayList<Books> bookses) {
        super(context, 0, bookses);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_books, parent, false);
        }
        Books currentBook = getItem(position);

        TextView bookTitle = (TextView) listView.findViewById(R.id.titleView);
        bookTitle.setText(currentBook.getTitle());

        TextView subTitile = (TextView) listView.findViewById(R.id.subtitleView);
        subTitile.setText(currentBook.getSubTitle());

        TextView author = (TextView) listView.findViewById(R.id.authorView);
        author.setText(currentBook.getAuthor());

        TextView dateView = (TextView) listView.findViewById(R.id.publishedView);
        dateView.setText(currentBook.getPubDate());

        return listView;
    }

    //Closing tag for main class
}
