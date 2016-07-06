package com.example.dell.nanodegree;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by dell on 7/5/2016.
 */
public class MovieObject implements Parcelable {
    String original_title, overview, language;
    String posterPath;

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    String release_date;
    double rating;//vote_average in api
    double popularity;
    int vote_count;
    boolean isAdult;

    public MovieObject() {
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.original_title);
        dest.writeString(this.overview);
        dest.writeString(this.language);
        dest.writeString(this.posterPath);
        dest.writeString(this.release_date);
        dest.writeDouble(this.rating);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.vote_count);
        dest.writeByte(this.isAdult ? (byte) 1 : (byte) 0);
    }

    protected MovieObject(Parcel in) {
        this.original_title = in.readString();
        this.overview = in.readString();
        this.language = in.readString();
        this.posterPath = in.readString();
        this.release_date = in.readString();
        this.rating = in.readDouble();
        this.popularity = in.readDouble();
        this.vote_count = in.readInt();
        this.isAdult = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MovieObject> CREATOR = new Parcelable.Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel source) {
            return new MovieObject(source);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };
}
