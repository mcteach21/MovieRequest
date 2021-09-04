package mc.apps.movies.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiInterface {
    @GET("discover/movie")
    Call<Result> list(@Query("api_key") String api_key ,@Query("year") int year, @Query("page") int page);
    @GET("search/movie")
    Call<Result> filter(@Query("api_key") String api_key , @Query("year") int year, @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page);
}
