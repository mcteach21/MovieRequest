package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowResult {
    @SerializedName("page")
    @Expose
    public Integer page;
    @SerializedName("results")
    @Expose
    public List<TvShow> results;
    @SerializedName("total_pages")
    @Expose
    public Integer totalPages;
    @SerializedName("total_results")
    @Expose
    public Integer totalResults;
}
