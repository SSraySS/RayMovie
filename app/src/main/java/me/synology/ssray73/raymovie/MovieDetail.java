package me.synology.ssray73.raymovie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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

    public MovieDetail(String movieId, String movieTitle, String poster_url, String release, String rate, String desc) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.poster_url = poster_url;
        this.release = release;
        this.rate = rate;
        this.desc = desc;
    }

    private MovieDetail(Parcel in) {
        movieId = in.readString();
        movieTitle = in.readString();
        poster_url = in.readString();
        release = in.readString();
        rate = in.readString();
        desc = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(movieTitle);
        dest.writeString(poster_url);
        dest.writeString(release);
        dest.writeString(rate);
        dest.writeString(desc);

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
