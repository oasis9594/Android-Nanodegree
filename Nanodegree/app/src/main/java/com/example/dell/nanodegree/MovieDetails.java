package com.example.dell.nanodegree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MovieDetails extends AppCompatActivity {

    MovieObject movieObject;
    ImageView poster;
    TextView overview, rating, votes, det_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        movieObject=(MovieObject) getIntent().getExtras().get(getResources().getString(R.string.parcelabel_movie_key));
        assert getSupportActionBar()!=null;
        getSupportActionBar().setTitle(movieObject.getOriginal_title());
        poster=(ImageView)findViewById(R.id.detail_poster_image);
        overview=(TextView) findViewById(R.id.detail_overview);
        rating=(TextView) findViewById(R.id.detail_rating);
        votes=(TextView) findViewById(R.id.detail_votes);
        det_date=(TextView)findViewById(R.id.detail_date);

        Picasso.with(poster.getContext()).load(movieObject.getPosterPath()).error(R.drawable.noposter).into(poster);
        overview.setText(movieObject.getOverview());
        String r=getResources().getString(R.string.rating)+": "+String.valueOf(movieObject.getRating());
        String v=getResources().getString(R.string.votes)+": "+String.valueOf(movieObject.getVote_count());
        rating.setText(r);
        votes.setText(v);

        DateFormat inputDF  = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date1 = null;
        try {
            date1 = inputDF.parse(movieObject.getRelease_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat f = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        det_date.setText(f.format(date1));
    }
}
