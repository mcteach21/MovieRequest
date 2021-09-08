package mc.apps.movies.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credit {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("cast")
    @Expose
    public List<Actor> cast = null;
    @SerializedName("crew")
    @Expose
    public List<Crew> crew = null;
}
