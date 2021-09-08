package mc.apps.movies.api;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
