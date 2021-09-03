package mc.apps.movies.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiInterface {
    String API_KEY="5956cfe9afe78bdf9b9c1449c3b8baf5";

    @GET("discover/movie?api_key="+API_KEY)
    Call<Result> list(@Query("year") int year);

    @GET("search/movie?api_key="+API_KEY)
    Call<Result> filter(@Query("year") int year, @Query("query") String keyword, @Query("include_adult") boolean adult);
}
