package me.synology.ssray73.raymovie;

/**
 * Created by raymond on 15/7/15.
 */
public class MovieDetail {
    private String movieId;
    private String movieTitle;
    private String poster_url;
    private String release;
    private String rate;
    private String desc;

    public MovieDetail(String movieId, String movieTitle, String poster_url, String release, String rate, String desc) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.poster_url = poster_url;
        this.release = release;
        this.rate = rate;
        this.desc = desc;
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
}
