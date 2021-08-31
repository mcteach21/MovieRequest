package mc.apps.movies;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import mc.apps.movies.tools.Movie;

public class MovieActivity extends AppCompatActivity {
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
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