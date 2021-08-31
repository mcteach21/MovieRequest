package mc.apps.movies.tools;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("page")
    public String page;
    @SerializedName("results")
    public List<Movie> movies;

    @Override
    public String toString() {
        return "Result{" +
                "page='" + page + '\'' +
                ", results=" + movies +
                '}';
    }
}
