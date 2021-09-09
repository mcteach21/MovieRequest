package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Biography {
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("also_known_as")
    @Expose
    public List<String> alsoKnownAs = null;
    @SerializedName("biography")
    @Expose
    public String biography;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("deathday")
    @Expose
    public Object deathday;
    @SerializedName("gender")
    @Expose
    public Integer gender;
    @SerializedName("homepage")
    @Expose
    public String homepage;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("imdb_id")
    @Expose
    public String imdbId;
    @SerializedName("known_for_department")
    @Expose
    public String knownForDepartment;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("place_of_birth")
    @Expose
    public String placeOfBirth;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("profile_path")
    @Expose
    public String profilePath;


}
