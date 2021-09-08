package mc.apps.movies.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result2 {
    @SerializedName("genres")
    public List<Genre> genres;
}
