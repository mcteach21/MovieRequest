package mc.apps.movies.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiInterface {
    String IMAGES_URL="https://image.tmdb.org/t/p/w500";

    @GET("discover/movie")
    Call<Result> list(@Query("api_key") String api_key, @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);

    @GET("discover/movie")
    Call<Result> byReleaseYear(@Query("api_key") String api_key, @Query("primary_release_year") int year, @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);
    @GET("discover/movie")
    Call<Result> byGenre(@Query("api_key") String api_key, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);

    @GET("discover/movie")
    Call<Result> byCasting(@Query("api_key") String api_key, @Query("with_cast") int actor_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);

    @GET("discover/movie")
    Call<Result> byGenreAndYear(@Query("api_key") String api_key, @Query("primary_release_year") int year, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);

    @GET("search/movie")
    Call<Result> byKeyword(@Query("api_key") String api_key , @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);
    @GET("search/movie")
    Call<Result> byKeywordAndYear(@Query("api_key") String api_key , @Query("primary_release_year") int year, @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by, @Query("language") String language);

    @GET("genre/movie/list")
    Call<Result2> genres(@Query("api_key") String api_key);

    @GET("search/person")
    Call<Result3> actor(@Query("api_key") String api_key, @Query("query") String firstName_lastName);

    @GET("movie/{movie_id}/credits")
    Call<Credit> casting(@Path(value="movie_id", encoded=false) int movie_id, @Query("api_key") String api_key);
}
