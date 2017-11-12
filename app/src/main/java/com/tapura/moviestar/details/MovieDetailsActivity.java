package com.tapura.moviestar.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tapura.moviestar.R;
import com.tapura.moviestar.databinding.ActivityMovieDetailsBinding;
import com.tapura.moviestar.model.ResultsItemMoviesBySort;

import org.parceler.Parcels;

import static com.tapura.moviestar.Constants.KEY_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();
        ResultsItemMoviesBySort movie = Parcels.unwrap(intent.getParcelableExtra(KEY_MOVIE));
        setTitle(movie.getTitle());

        binding.setMovie(movie);

        ImageView iv = findViewById(R.id.imageview_movie_posterpath);
        Picasso.with(this).load(movie.getBackdropPath()).into(iv);
    }
}
