package me.synology.ssray73.raymovie.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raymond on 19/8/15.
 */
public class MovieReview implements Parcelable {

    @SerializedName("id")
    String reviewerId;

    @SerializedName("author")
    String author;

    @SerializedName("content")
    String content;

    @SerializedName("url")
    String url;

    public MovieReview(String reviewerId, String author, String content, String url) {
        this.reviewerId = reviewerId;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    protected MovieReview(Parcel in) {
        reviewerId = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewerId);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}


