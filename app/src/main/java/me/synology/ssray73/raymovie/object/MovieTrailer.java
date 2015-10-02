package me.synology.ssray73.raymovie.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raymond on 19/8/15.
 */
public class MovieTrailer implements Parcelable {

    @SerializedName("id")
    String trailerID;

    @SerializedName("iso_639_1")
    String iso;

    @SerializedName("key")
    String key;

    @SerializedName("name")
    String name;

    @SerializedName("site")
    String site;

    @SerializedName("size")
    String size;

    @SerializedName("type")
    String type;


    public MovieTrailer(String trailerID, String iso, String key, String name, String site, String size, String type) {
        this.trailerID = trailerID;
        this.iso = iso;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getTrailerID() {
        return trailerID;
    }

    public String getIso() {
        return iso;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    protected MovieTrailer(Parcel in) {
        trailerID = in.readString();
        iso = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerID);
        dest.writeString(iso);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
    }
}
