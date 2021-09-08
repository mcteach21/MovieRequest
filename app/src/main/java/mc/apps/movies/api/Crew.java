package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Crew {

    @SerializedName("adult")
    @Expose
    public Boolean adult;
    @SerializedName("gender")
    @Expose
    public Integer gender;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("known_for_department")
    @Expose
    public String knownForDepartment;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("original_name")
    @Expose
    public String originalName;
    @SerializedName("popularity")
    @Expose
    public Double popularity;
    @SerializedName("profile_path")
    @Expose
    public String profilePath;
    @SerializedName("credit_id")
    @Expose
    public String creditId;
    @SerializedName("department")
    @Expose
    public String department;
    @SerializedName("job")
    @Expose
    public String job;


}
