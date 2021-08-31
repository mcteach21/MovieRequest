package mc.apps.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mc.apps.movies.tools.Movie;
import mc.apps.movies.tools.RestApiClient;
import mc.apps.movies.tools.RestApiInterface;
import mc.apps.movies.tools.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "retrofit";
    private MyCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnStart = findViewById(R.id.btnStartHttpClient);
        btnStart.setOnClickListener(v-> startApiCall());

        handleRecyclerview();
    }

    private void startApiCall() {

        RestApiInterface apiInterface = RestApiClient.getInstance();
        Call<Result> call = apiInterface.list(); //&year=2021
        call.enqueue(new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.i(TAG , "***************************************");
                Result result = response.body();
                Log.i(TAG , "Response : "+result);
                Log.i(TAG , "***************************************");

                adapter.reset();
                List<Movie> movies = result.movies;
                if(response.isSuccessful()) {

                    movies.forEach(movie -> Log.i(TAG , String.valueOf(movie)));
                    movies.forEach(movie -> adapter.add(movie));
                } else {
                    Log.i(TAG , String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG , t.getMessage());
            }
        });
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
                Toast.makeText(this, "Nothing implemented yet! :)", Toast.LENGTH_SHORT).show();
                break;
            default:
                finish();
        }
        return true;
    }


    /**
     * List
     */
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

    interface ListItemClickListener {
        void onClick(int position);
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

            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500"+movie.poster_path)
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