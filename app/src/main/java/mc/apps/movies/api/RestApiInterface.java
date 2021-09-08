package mc.apps.movies.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiInterface {
    String IMAGES_URL="https://image.tmdb.org/t/p/w500";

    @GET("discover/movie")
    Call<Result> list(@Query("api_key") String api_key, @Query("page") int page, @Query("sort_by") String sort_by);

    @GET("discover/movie")
    Call<Result> byReleaseYear(@Query("api_key") String api_key, @Query("primary_release_year") int year, @Query("page") int page, @Query("sort_by") String sort_by);
    @GET("discover/movie")
    Call<Result> byGenre(@Query("api_key") String api_key, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by);
    @GET("discover/movie")
    Call<Result> byGenreAndYear(@Query("api_key") String api_key, @Query("primary_release_year") int year, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by);


    @GET("search/movie")
    Call<Result> byKeyword(@Query("api_key") String api_key , @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by);
    @GET("search/movie")
    Call<Result> byKeywordAndYear(@Query("api_key") String api_key , @Query("primary_release_year") int year, @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by);

    @GET("genre/movie/list")
    Call<Result2> genres(@Query("api_key") String api_key);
}
