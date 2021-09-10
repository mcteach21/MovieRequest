package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Media implements Serializable {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("original_language")
    @Expose
    public String originalLanguage;
    @SerializedName("original_title")
    @Expose
    public String originalTitle;

    @SerializedName("first_air_date")
    public String firstAirDate=null;
    @SerializedName("origin_country")
    public List<String> originCountry = null;

    @SerializedName("overview")
    @Expose
    public String overview;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("poster_path")
    @Expose
    public String posterPath;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("video")
    @Expose
    public Boolean video;
    @SerializedName("vote_average")
    @Expose
    public Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    public Integer voteCount;
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    public String backdropPath;
    @SerializedName("genre_ids")
    @Expose
    public List<Integer> genreIds = null;

    @SerializedName("name")
    public String name;
    @SerializedName("original_name")
    public String originalName=null;

//    @SerializedName("media_type")
//    public String mediaType=null;

    public String getTitle() {
        String _title = (title==null)?name:title;
        String _originaltitle = (originalTitle==null)?originalName:originalTitle;

        return _title.equalsIgnoreCase(_originaltitle)?_title:_title+" ["+_originaltitle+"]";
    }
    public String getSubTitle() {
        return originalLanguage+" | "+releaseDate!=null?releaseDate:firstAirDate;
    }

    @Override
    public String toString() {
        return "Media {" +
                "adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", genreIds=" + genreIds +
                ", id=" + id +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", name='" + name + '\'' +
                ", firstAirDate='" + firstAirDate + '\'' +
                ", originCountry=" + originCountry +
                ", originalName='" + originalName + '\'' +
                '}';
    }
}
