package com.example.dell.nanodegree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularMovieAdapter extends RecyclerView.Adapter<PopularMovieAdapter.ViewHolder> {

    ArrayList<MovieObject> myMovies;
    Context mContext;
    public PopularMovieAdapter( Activity activity, ArrayList<MovieObject> obj) {
        this.myMovies=obj;
        this.mContext=activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        MyClickHandler mlistener;
        ImageView moviePoster;
        public ViewHolder(View itemView, MyClickHandler listener) {
            super(itemView);
            mlistener=listener;
            moviePoster=(ImageView)itemView.findViewById(R.id.movie_poster_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mlistener.showMovieDetails(v, getAdapterPosition());
        }

        public interface MyClickHandler {
            void showMovieDetails(View v, int pos);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(PopularMovies.TAG, "onCreateViewHolder1");
        View customView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_movie_row, parent, false);
        ViewHolder vh= new ViewHolder(customView, new ViewHolder.MyClickHandler() {

            @Override
            public void showMovieDetails(View v, int pos) {
                mContext.startActivity(new Intent(mContext, MovieDetails.class).
                        putExtra(mContext.getResources().getString(R.string.parcelabel_movie_key), myMovies.get(pos)));
            }
        });
        Log.v(PopularMovies.TAG, "onCreateViewHolder2");
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.v(PopularMovies.TAG, "onBindViewHolder1");
        MovieObject movieObject=myMovies.get(position);
        Picasso.with(holder.moviePoster.getContext()).load(movieObject.getPosterPath()).error(R.drawable.noposter).into(holder.moviePoster);
        Log.v(PopularMovies.TAG, "onBindViewHolder2");
    }

    @Override
    public int getItemCount() {
        return myMovies.size();
    }

}
