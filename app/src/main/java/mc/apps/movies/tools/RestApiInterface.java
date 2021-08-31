package mc.apps.movies.tools;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiInterface {
    @GET("quey={query}")
    Call<List<Movie>> lastMovies(@Query("query") String query);

    @GET("movie?api_key=5956cfe9afe78bdf9b9c1449c3b8baf5")
    //Call<List<Result>> test(@Query("query") String query);
    Call<ResponseBody> demo();

    @GET("movie?api_key=5956cfe9afe78bdf9b9c1449c3b8baf5&year=2021")
    Call<Result> list();
}
