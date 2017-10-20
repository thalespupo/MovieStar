package com.tapura.moviestar.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tapura.moviestar.R;
import com.tapura.moviestar.databinding.ActivityMovieDetailsBinding;
import com.tapura.moviestar.model.ResultsItem;

import org.parceler.Parcels;

import static com.tapura.moviestar.Constants.KEY_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();
        ResultsItem movie = Parcels.unwrap(intent.getParcelableExtra(KEY_MOVIE));
        setTitle(movie.getTitle());

        binding.setMovie(movie);



    }

}
