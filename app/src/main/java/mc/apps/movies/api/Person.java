package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Person {
    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("gender")
    @Expose
    public Integer gender;
    @SerializedName("id")
    @Expose
    public Integer id;
//    @SerializedName("known_for")
//    @Expose
//    public List<KnownFor> knownFor;
    @SerializedName("known_for_department")
    @Expose
    public String knownForDepartment;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("profile_path")
    @Expose
    public Object profilePath;

}
