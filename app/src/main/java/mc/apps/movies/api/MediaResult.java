package mc.apps.movies.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaResult {
    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Media> medias;

    @SerializedName("total_pages")
    public int pages;
    @SerializedName("total_results")
    public int total;

    @Override
    public String toString() {
        return page + ": " + medias;
    }
}
