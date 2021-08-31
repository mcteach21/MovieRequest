package mc.apps.movies.tools;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("adult")
    public boolean adult;
    @SerializedName("original_title")
    public String original_title;
    @SerializedName("title")
    public String title;
    @SerializedName("release_date")
    public String release_date;

    @SerializedName("original_language")
    public String original_language;

    @SerializedName("overview")
    public String overview;
    @SerializedName("backdrop_path")
    public String backdrop_path;
    @SerializedName("poster_path")
    public String poster_path;

    //    "genre_ids":[28,12,14,35],
    //    "popularity":3692.281,
    //    "video":false,
    //    "vote_average":8,
    //    "vote_count":3201

    @Override
    public String toString() {
        return "[" + id + "] " + title +  " | " + release_date;
    }
}
