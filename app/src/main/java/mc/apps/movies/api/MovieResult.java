package mc.apps.movies.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult {
    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Movie> movies;

    @SerializedName("total_pages")
    public int pages;
    @SerializedName("total_results")
    public int total;

    @Override
    public String toString() {
        return "Result{" +
                "page='" + page + '\'' +
                ", results=" + movies +
                '}';
    }
}
