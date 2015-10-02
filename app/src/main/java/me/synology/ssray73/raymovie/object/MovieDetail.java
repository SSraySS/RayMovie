package me.synology.ssray73.raymovie.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import static me.synology.ssray73.raymovie.object.MovieReview.*;

/**
 * Created by raymond on 15/7/15.
 */
public class MovieDetail implements Parcelable {

    @SerializedName("id")
    String movieId;

    @SerializedName("original_title")
    String movieTitle;

    @SerializedName("poster_path")
    String poster_url;

    @SerializedName("release_date")
    String release;

    @SerializedName("vote_average")
    String rate;

    @SerializedName("overview")
    String desc;

    ArrayList<MovieReview> reviews;

    ArrayList<MovieTrailer> trailers;

    public MovieDetail(String movieId, String movieTitle, String poster_url, String release,
                       String rate, String desc, ArrayList<MovieReview> reviews,
                       ArrayList<MovieTrailer> trailers) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.poster_url = poster_url;
        this.release = release;
        this.rate = rate;
        this.desc = desc;
        this.reviews = reviews;
        this.trailers = trailers;
    }

    private MovieDetail(Parcel in) {
        movieId = in.readString();
        movieTitle = in.readString();
        poster_url = in.readString();
        release = in.readString();
        rate = in.readString();
        desc = in.readString();
        in.readTypedList(reviews, MovieReview.CREATOR);
        in.readTypedList(trailers, MovieTrailer.CREATOR);

    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<MovieReview> reviews) {
        this.reviews = reviews;
    }


    public ArrayList<MovieTrailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(movieTitle);
        dest.writeString(poster_url);
        dest.writeString(release);
        dest.writeString(rate);
        dest.writeString(desc);
        dest.writeTypedList(reviews);
        dest.writeTypedList(trailers);
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR
            = new Parcelable.Creator<MovieDetail>() {
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}
