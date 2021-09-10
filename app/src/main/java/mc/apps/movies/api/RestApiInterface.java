package mc.apps.movies.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiInterface {
    String IMAGES_URL="https://image.tmdb.org/t/p/w500";

    @GET("discover/movie")
    Call<MediaResult> multi1(@Query("api_key") String api_key, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/tv")
    Call<MediaResult> multi2(@Query("api_key") String api_key, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);


    @GET("discover/movie")
    Call<MediaResult> list(@Query("api_key") String api_key, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MediaResult> byReleaseYear(@Query("api_key") String api_key, @Query("primary_release_year") int year, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/movie")
    Call<MediaResult> byGenre(@Query("api_key") String api_key, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MediaResult> byGenreAndYear(@Query("api_key") String api_key, @Query("primary_release_year") int year, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    @GET("search/movie")
    Call<MediaResult> byKeyword(@Query("api_key") String api_key , @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("search/movie")
    Call<MediaResult> byKeywordAndYear(@Query("api_key") String api_key , @Query("primary_release_year") int year, @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    @GET("genre/movie/list")
    Call<Result2> genres(@Query("api_key") String api_key);

    @GET("search/person")
    Call<Result3> actor(@Query("api_key") String api_key, @Query(value="query", encoded=false) String firstName_lastName);

    @GET("person/{casting_id}")
    Call<Biography> actor(@Path(value="casting_id", encoded=false) int casting_id, @Query("api_key") String api_key);

    @GET("movie/{movie_id}/credits")
    Call<Credit> credits(@Path(value="movie_id", encoded=false) int movie_id, @Query("api_key") String api_key);

    @GET("discover/movie")
    Call<MediaResult> byCastingAndGenreAndYear(@Query("api_key") String api_key, @Query("with_cast") int casting_id, @Query("primary_release_year") int year, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/movie")
    Call<MediaResult> byCastingAndGenre(@Query("api_key") String api_key, @Query("with_cast") int casting_id, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/movie")
    Call<MediaResult> byCastingAndYear(@Query("api_key") String api_key, @Query("with_cast") int casting_id, @Query("primary_release_year") int year, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    @GET("discover/movie")
    Call<MediaResult> byCasting(@Query("api_key") String api_key, @Query("with_cast") int casting_id, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    // exemples  : https://www.themoviedb.org/documentation/api/discover
    // discover/movie?with_people=287,819&sort_by=vote_average.desc
    // discover/movie?primary_release_year=2010&sort_by=vote_average.desc

    /**
     * Tv Shows
     */
    @GET("tv/popular")
    Call<MediaResult> tvPopular(@Query("api_key") String api_key, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/tv")
    Call<MediaResult> tvByGenre(@Query("api_key") String api_key, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/tv")
    Call<MediaResult> tvByYear(@Query("api_key") String api_key, @Query("first_air_date_year") int year, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("discover/tv")
    Call<MediaResult> tvByGenreAndYear(@Query("api_key") String api_key, @Query("first_air_date_year") int year, @Query("with_genres") int genre_id , @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);

    @GET("search/tv")
    Call<MediaResult> tvByKeyword(@Query("api_key") String api_key , @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);
    @GET("search/tv")
    Call<MediaResult> tvByKeywordAndYear(@Query("api_key") String api_key , @Query("first_air_date_year") int year, @Query("query") String keyword, @Query("include_adult") boolean adult, @Query("page") int page, @Query("sort_by") String sort_by, @Query("with_original_language") String language);


    @GET("tv/{tvshow_id}/credits")
    Call<Credit> tvCredits(@Path(value="tvshow_id", encoded=false) int tvshow_id, @Query("api_key") String api_key);
}
