package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShow extends Media{
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("first_air_date")
    @Expose
    public String firstAirDate;
    @SerializedName("genre_ids")
    @Expose
    public List<Integer> genreIds = null;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("origin_country")
    @Expose
    public List<String> originCountry;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_name")
    @Expose
    public String originalName;
    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getTitle() {
        return name.equalsIgnoreCase(originalName)?name:name+" ["+originalName+"]";
    }
    @Override
    public String getSubTitle() {
        return originCountry+" | "+ originalLanguage+" | "+firstAirDate;
    }
    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public String getLogoPath() {
        return backdropPath;
    }
}
