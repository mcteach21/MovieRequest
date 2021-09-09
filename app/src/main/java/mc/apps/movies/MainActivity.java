package mc.apps.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mc.apps.movies.api.GenresManager;
import mc.apps.movies.api.Movie;
import mc.apps.movies.api.Person;
import mc.apps.movies.api.RestApiClient;
import mc.apps.movies.api.RestApiInterface;
import mc.apps.movies.api.Result;
import mc.apps.movies.api.Result3;
import mc.apps.movies.tools.Animate;
import mc.apps.movies.tools.Dialogs;
import mc.apps.movies.tools.ListItemClickListener;
import mc.apps.movies.tools.Ui;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "retrofit";
    private static final String MOVIES_SORT_BY = "primary_release_date.desc";
    private static final String MOVIES_ORIGINAL_LANGUAGE = "fr|en";
    private MyCustomAdapter adapter;
    private TextView txtInfo;

    RestApiInterface apiInterface;

    int thisYear = Calendar.getInstance().get(Calendar.YEAR);
    SharedPreferences sharedPreferences;
    String moviesAPIKey;

    private String[] lastCall= new String[5];
    Button btnPrev, btnNext;
    int currentPage=1, totalPages=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        apiInterface = RestApiClient.getInstance();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(checkSettings()) {
            init();
            if(lastCall[0]!=null)
                //year,keyword, adult,genre_id, casting
                MoviesApiCall(Integer.parseInt(lastCall[0]) ,lastCall[1], Boolean.parseBoolean(lastCall[2]), currentPage, Integer.parseInt(lastCall[3]), lastCall[4]);
        }

    }
    private boolean checkSettings() {
        //check api keys
        moviesAPIKey = sharedPreferences.getString("edt_pref_movies_apikey","");
        String firebaseAPIKey = sharedPreferences.getString("edt_pref_firebase_apikey","");
        return !moviesAPIKey.isEmpty() && !firebaseAPIKey.isEmpty();
    }
    private void init() {
        TextView warning = findViewById(R.id.txtWarning);
        warning.setVisibility(View.GONE);

        Button btnStart = findViewById(R.id.btnStartHttpClient);

        Boolean allowAdult = sharedPreferences.getBoolean("swc_prefs_allow_adult", false);
        btnStart.setOnClickListener(v->
                        {   resetPaginate(); MoviesApiCall(thisYear,"", allowAdult, 1,0, "");}
                ); //
        btnStart.setEnabled(true);

        handleRecyclerview();

        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        btnPrev.setOnClickListener(v->managePage(-1));
        btnNext.setOnClickListener(v->managePage(1));
        txtInfo = findViewById(R.id.txtInfo);


        ImageView filter = findViewById(R.id.imgFilter);
        Animate.rotateView(filter,1500,true);
        filter.setOnClickListener(v-> startFilterDialog());
    }

    private void managePage(int i) {
        int num = currentPage+i;
        currentPage = (num>totalPages || num==0)?1:num;

        //if(lastCall[0]!=null)
        MoviesApiCall(Integer.parseInt(lastCall[0]) ,lastCall[1], Boolean.parseBoolean(lastCall[2]), currentPage, Integer.parseInt(lastCall[3]), lastCall[4]);
    }

    private void startFilterDialog() {
        Dialogs.showCustomDialog(this,R.layout.filter_layout,"","Apply","",
                (dialog, witch) -> applyFilyter(dialog),
                dialogInterface -> populateSpinner(dialogInterface));
    }
    private void populateSpinner(DialogInterface dialog) {
        AlertDialog view = ((AlertDialog) dialog);
        AppCompatAutoCompleteTextView spinYear = view.findViewById(R.id.yearsAutoComplete);

        AppCompatAutoCompleteTextView spinGenre = view.findViewById(R.id.genresAutoComplete);
        GenresManager.Populate(MainActivity.this, apiInterface, moviesAPIKey, spinGenre);

        ArrayList<String> years = new ArrayList<String>();
        years.add("---");
        for (int i = thisYear+1; i >=2000 ; i--)
            years.add(Integer.toString(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        spinYear.setAdapter(adapter);

        Button button = view.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_apply_filter, 0, 0, 0);
        Ui.centerImageAndTextInButton(button);
    }
    private void applyFilyter(DialogInterface dialog) {
        AlertDialog view = ((AlertDialog) dialog);

        EditText edtKeyword = view.findViewById(R.id.edtKeyword);
        AppCompatAutoCompleteTextView spinYear = view.findViewById(R.id.yearsAutoComplete);
        AppCompatAutoCompleteTextView spinGenre = view.findViewById(R.id.genresAutoComplete);
        EditText edtCasting = view.findViewById(R.id.edtCasting);

        Boolean allowAdult = sharedPreferences.getBoolean("swc_prefs_allow_adult", false);

        int genre =0, year=0;
        try {
            genre = Integer.parseInt(spinGenre.getText().toString().split(" - ")[0]);
        }catch(Exception e){}
        try {
            year = Integer.parseInt(spinYear.getText().toString());
        }catch(Exception e){}

        resetPaginate();

        String keyword=edtKeyword.getEditableText().toString();
        String casting=edtCasting.getEditableText().toString().trim().replace(" ","+");

        MoviesApiCall(year, keyword, allowAdult, currentPage, genre, casting);
    }

    private void resetPaginate() {
        currentPage=1;
        totalPages=0;
        txtInfo.setText("");
    }

    private void MoviesApiCall(int year, String keyword, boolean adult, int page, int genre_id, String casting) {
        saveLastCall(year,keyword, adult, genre_id, casting);
        if(!"".equals(casting)){
            Call<Result3> call0 = apiInterface.actor(moviesAPIKey, casting);
            call0.enqueue(new Callback<Result3>() {
                @Override
                public void onResponse(Call<Result3> call0, Response<Result3> response) {
                    Result3 result = response.body();
                    if(result!=null) {
                        List<Person> casting = result.results;
                        int casting_id = !casting.isEmpty()?casting.get(0).id:0;

                        Call<Result> call;
                        if (genre_id > 0) {
                            call = year == 0 ?
                                    apiInterface.byCastingAndGenre(moviesAPIKey, casting_id, genre_id, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE)
                                    : apiInterface.byCastingAndGenreAndYear(moviesAPIKey, casting_id, year, genre_id, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE);
                        }else{
                            call = year == 0 ?
                                    apiInterface.byCasting(moviesAPIKey, casting_id, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE)
                                    : apiInterface.byCastingAndYear(moviesAPIKey, casting_id, year, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE);
                        }
                        //  call = keyword.isEmpty() ? (year == 0 ? apiInterface.list(moviesAPIKey, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE) : apiInterface.byReleaseYear(moviesAPIKey, year, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE)) :
                        //  (year == 0 ? apiInterface.byKeyword(moviesAPIKey, keyword, adult, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE) : apiInterface.byKeywordAndYear(moviesAPIKey, year, keyword, adult, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE));

                        callApi(call);
                    }else{
                        Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Result3> call, Throwable t) {
                    Log.e(TAG , "Error : "+t.getMessage());
                }
            });
        }else {
            Call<Result> call;
            if (genre_id > 0)
                call = year == 0 ? apiInterface.byGenre(moviesAPIKey, genre_id, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE) : apiInterface.byGenreAndYear(moviesAPIKey, year, genre_id, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE);
            else
                call = keyword.isEmpty() ? (year == 0 ? apiInterface.list(moviesAPIKey, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE) : apiInterface.byReleaseYear(moviesAPIKey, year, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE)) :
                        (year == 0 ? apiInterface.byKeyword(moviesAPIKey, keyword, adult, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE) : apiInterface.byKeywordAndYear(moviesAPIKey, year, keyword, adult, page, MOVIES_SORT_BY, MOVIES_ORIGINAL_LANGUAGE));
            callApi(call);
        }
    }

    private void callApi(Call<Result> call) {
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result = response.body();
                if (result != null) {
                    currentPage = result.page;
                    totalPages = result.pages;
                    txtInfo.setText("page " + currentPage + " sur " + result.pages + " [" + result.total + "]");

                    adapter.reset();
                    List<Movie> movies = result.movies;
                    if (response.isSuccessful()) {
                        movies.forEach(movie -> adapter.add(movie));
                    } else {
                        Log.i(TAG, String.valueOf(response.errorBody()));
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.infoLayout).setVisibility(result != null ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void saveLastCall(int year, String keyword, boolean adult, int genre_id, String casting){
        lastCall[0]= String.valueOf(year);
        lastCall[1]= keyword;
        lastCall[2]= String.valueOf(adult);
        lastCall[3]= String.valueOf(genre_id);
        lastCall[4]= casting;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                finish();
        }
        return true;
    }
    private void handleRecyclerview() {
        RecyclerView recyclerview = findViewById(R.id.list);
        ListItemClickListener item_listener = (id) -> {
            //Toast.makeText(this, "You clicked item #"+id, Toast.LENGTH_SHORT).show();

            Movie movie = adapter.items.get(id);
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra("movie", movie);

            startActivity(intent);
        };

        adapter = new MyCustomAdapter(item_listener);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation_fall_down);
        recyclerview.setLayoutAnimation(animation);
    }

    private class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyCustomViewHolder> {
        private static final long FADE_DURATION = 2000;
        List<Movie> items = new ArrayList<Movie>();

        private ListItemClickListener listener;
        public MyCustomAdapter(ListItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item_view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_layout, parent, false);
            return new MyCustomViewHolder(item_view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCustomViewHolder holder, int position) {
            Movie movie = items.get(position);

            holder.title.setText(movie.title.equalsIgnoreCase(movie.original_title)?movie.title:movie.title+" ["+movie.original_title+"]");
            holder.subtitle.setText(movie.original_language+" | "+movie.release_date);
            holder.desc.setText(movie.overview);

            Log.i(TAG, movie.overview);

            Picasso.get()
                    .load(RestApiInterface.IMAGES_URL+movie.poster_path)
//                    .resize(80, 80)
//                    .centerCrop()
                    .into(holder.logo);

            setFadeAnimation(holder.itemView);
            setScaleAnimation(holder.itemView);
        }

        /**
         * Animtions
         * @return
         */
        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }
        private void setScaleAnimation(View view) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(FADE_DURATION/2);
            view.startAnimation(anim);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void reset() {
            this.items.clear();
            notifyDataSetChanged();
        }

        public void add(Movie movie) {
            this.items.add(movie);
            notifyDataSetChanged();
        }

        class MyCustomViewHolder extends RecyclerView.ViewHolder {
            TextView title, subtitle, desc;
            ImageView logo;
            public MyCustomViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvTitle);
                subtitle = itemView.findViewById(R.id.tvSubTitle);
                desc = itemView.findViewById(R.id.tvDesc);
                logo = itemView.findViewById(R.id.tvLogo);

                itemView.setOnClickListener(v->listener.onClick(getAdapterPosition()));
            }
        }
    }
}