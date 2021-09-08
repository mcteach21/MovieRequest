package mc.apps.movies;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mc.apps.movies.api.Actor;
import mc.apps.movies.api.Casting;
import mc.apps.movies.api.Credit;
import mc.apps.movies.api.Crew;
import mc.apps.movies.api.Movie;
import mc.apps.movies.api.RestApiClient;
import mc.apps.movies.api.RestApiInterface;
import mc.apps.movies.tools.ListItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private static final String TAG = "retrofit";
    TextView desc;
    Button btnCasting;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        ImageView logo = findViewById(R.id.tvLogo);
        TextView title = findViewById(R.id.tvTitle);
        TextView subtitle = findViewById(R.id.tvSubTitle);
        desc = findViewById(R.id.tvDesc);

        title.setText(movie.title.equalsIgnoreCase(movie.original_title)?movie.title:movie.title+" ["+movie.original_title+"]");
        subtitle.setText(movie.original_language+" | "+movie.release_date);
        desc.setText(movie.overview);

        desc.setOnClickListener((v)->translate());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            desc.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        Picasso.get()
                .load("https://image.tmdb.org/t/p/w500"+movie.poster_path)
                .into(logo);

        FirebaseApp.initializeApp(this);

        //Casting (List..)
        handleRecyclerview();
        btnCasting = findViewById(R.id.btnCasting);
        btnCasting.setOnClickListener(v->GetMovieCasting());
    }

    private void GetMovieCasting() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MovieActivity.this);
        String moviesAPIKey = sharedPreferences.getString("edt_pref_movies_apikey","");
        RestApiInterface apiInterface = RestApiClient.getInstance();
        Call<Credit> call = apiInterface.casting(movie.id, moviesAPIKey);
        call.enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(Call<Credit> call, Response<Credit> response) {
                Credit result = response.body();
                if(result!=null) {
                    Log.i(TAG, "***************************************");
                    Log.i(TAG, "Response : " + result);
                    Log.i(TAG, "***************************************");
                    adapter.reset();

                    List<Actor> actors = result.cast;
                    List<Crew> crew = result.crew;

                    if (response.isSuccessful()) {
                        actors.forEach(c -> adapter.add(
                                new Casting(c.id,c.knownForDepartment,c.name,
                                        c.profilePath,c.originalName,c.character,
                                        "","")
                        ));
                        crew.forEach(c -> adapter.add(
                                new Casting(c.id,c.knownForDepartment,c.name,
                                        c.profilePath,c.originalName,"",
                                        c.department,c.job)
                        ));
                    } else {
                        Log.i(TAG, String.valueOf(response.errorBody()));
                    }
                }else{
                    Toast.makeText(MovieActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Credit> call, Throwable t) {
                Log.e(TAG , t.getMessage());
            }
        });
    }

    /**
     * List
     */
    private MyCustomAdapter adapter;
    private class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyCustomViewHolder> {
        private static final long FADE_DURATION = 2000;
        List<Casting> items = new ArrayList<>();

        private ListItemClickListener listener;
        public MyCustomAdapter(ListItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyCustomAdapter.MyCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item_view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_layout, parent, false);
            return new MyCustomAdapter.MyCustomViewHolder(item_view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyCustomAdapter.MyCustomViewHolder holder, int position) {
            Casting cast = items.get(position);

            String char_job = !cast.job.trim().equals("")?cast.department+" | "+cast.job:cast.character;
            holder.title.setText(cast.name.equalsIgnoreCase(cast.originalName)?cast.name:cast.name+" ["+cast.originalName+"]");
            holder.subtitle.setText(cast.knownForDepartment+" ("+char_job+")");
            //holder.desc.setText(cast.department);

            Picasso.get()
                    .load(RestApiInterface.IMAGES_URL+cast.profilePath)
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

        public void add(Casting cast) {
            this.items.add(cast);
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
    private void handleRecyclerview() {
        RecyclerView recyclerview = findViewById(R.id.listCasting);
        ListItemClickListener item_listener = (id) -> {
            Toast.makeText(this, "You clicked item #"+id, Toast.LENGTH_SHORT).show();
        };

        adapter = new MyCustomAdapter(item_listener);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_animation_fall_down);
        recyclerview.setLayoutAnimation(animation);
    }

    /**
     * Translation
     */
    public void translate() {
        translateTextToFrench(desc.getText().toString());
    }

    public void translateText(FirebaseTranslator langTranslator) {
        //translate source text to english
        langTranslator.translate(desc.getText().toString())
                .addOnSuccessListener(translatedText -> desc.setText(translatedText))
                .addOnFailureListener(
                        e -> Toast.makeText(MovieActivity.this,
                                "Problem in translating the text entered",
                                Toast.LENGTH_LONG).show()
                );

    }

    public void downloadTranslatorAndTranslate(String langCode) {
        //get source language id from bcp code
        int sourceLanguage = FirebaseTranslateLanguage.languageForLanguageCode(langCode);

        //create translator for source and target languages
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguage)
                        .setTargetLanguage(FirebaseTranslateLanguage.FR)
                        .build();
        final FirebaseTranslator langTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        //download language models if needed
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        langTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Log.d("translator", "downloaded lang  model");
                                //after making sure language models are available
                                //perform translation
                                translateText(langTranslator);
                            }
                        })
                .addOnFailureListener(
                        e -> Toast.makeText(MovieActivity.this,
                                "Problem in translating the text entered",
                                Toast.LENGTH_LONG).show());
    }

    public void translateTextToFrench(String text) {
        //FirebaseApp.initializeApp(MovieActivity.this);
        //First identify the language of the entered text
        FirebaseLanguageIdentification languageIdentifier = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode != "und") {
                                    Log.d("translator", "lang "+languageCode);
                                    //download translator for the identified language
                                    // and translate the entered text into english
                                    downloadTranslatorAndTranslate(languageCode);
                                } else {
                                    Toast.makeText(MovieActivity.this,
                                            "Could not identify language of the text entered",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MovieActivity.this,
                                        "Problem in identifying language of the text entered",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
    }
}