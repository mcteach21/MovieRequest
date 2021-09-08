package mc.apps.movies.api;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenresManager {
    private static List<Genre> genres=null;
    public static void Populate(Context context, RestApiInterface apiInterface, String moviesAPIKey, AppCompatAutoCompleteTextView spinner){
        if(genres==null){

            Log.i("retrofit" , "Populate: api call");
            Call<Result2> call = apiInterface.genres(moviesAPIKey);
            call.enqueue(new Callback<Result2>(){
                @Override
                public void onResponse(Call<Result2> call, Response<Result2> response) {
                    Result2 result = response.body();
                    genres = result.genres;

                    SetAdapter(context, spinner);
                }
                @Override
                public void onFailure(Call<Result2> call, Throwable t) {}
            });
        }else{
            Log.i("retrofit" , "Populate: no api call");
            SetAdapter(context, spinner);
        }
    }

    private static void SetAdapter(Context context, AppCompatAutoCompleteTextView spinner) {
        ArrayAdapter<Genre> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, genres);
        spinner.setAdapter(adapter);
    }
}
