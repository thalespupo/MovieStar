package com.tapura.moviestar.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tapura.moviestar.R;
import com.tapura.moviestar.model.ResultsItem;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import static com.tapura.moviestar.Constants.KEY_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        ResultsItem movie = Parcels.unwrap(intent.getParcelableExtra(KEY_MOVIE));
        TextView tv = findViewById(R.id.text_example);
        tv.setText(movie.getOriginalTitle());
    }

}
