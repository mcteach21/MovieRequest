package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Casting {
    public Integer id;
    public String knownForDepartment;
    public String name;
    public String profilePath;
    public String originalName;
    public String character;
    public String department;
    public String job;

    public Casting(Integer id, String knownForDepartment, String name, String profilePath, String originalName, String character, String department, String job) {
        this.id = id;
        this.knownForDepartment = knownForDepartment;
        this.name = name;
        this.profilePath = profilePath;
        this.originalName = originalName;
        this.character = character;
        this.department = department;
        this.job = job;
    }
}
