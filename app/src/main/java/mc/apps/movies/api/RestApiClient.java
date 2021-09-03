package mc.apps.movies.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {
    //https://image.tmdb.org/t/p/w500/8yhWlFcJ8zCqjfCvLy3lWFuawR1.jpg

    private static final String BASE_URL = "https://api.themoviedb.org/3/";//discover/";
    //private static final String BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/";

    private static final String TAG = "retrofit";
    public static RestApiInterface getInstance() {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiInterface apiInterface = retrofit.create(RestApiInterface.class);
        return apiInterface;
    }

//    public void start(){
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        RestApiInterface apiInterface = retrofit.create(RestApiInterface.class);
//
//        Call<List<Repo>> call = apiInterface.listRepos("mcteach21");
//        call.enqueue(new Callback<List<Repo>>() {
//            @Override
//            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
//
//                if(response.isSuccessful()) {
//                    List<Repo> repos = response.body();
//                    repos.forEach(repo -> Log.i(TAG , String.valueOf(repo)));
//                } else {
//                    Log.i(TAG , String.valueOf(response.errorBody()));
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Repo>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//
//    }
}
