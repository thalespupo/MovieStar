package com.tapura.moviestar.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tapura.moviestar.R;
import com.tapura.moviestar.api.MoviesAPIService;
import com.tapura.moviestar.api.MoviesAPIServiceBuilder;
import com.tapura.moviestar.databinding.ActivityMovieDetailsBinding;
import com.tapura.moviestar.model.ResponseReviewsFromMovie;
import com.tapura.moviestar.model.ResponseVideosFromMovie;
import com.tapura.moviestar.model.ResultsItemMoviesBySort;
import com.tapura.moviestar.model.ResultsItemReviewsFromMovie;
import com.tapura.moviestar.model.ResultsItemVideosFromMovie;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.moviestar.BuildConfig.API_KEY;
import static com.tapura.moviestar.Constants.APP_TAG;
import static com.tapura.moviestar.Constants.KEY_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String CLASS_TAG = MovieDetailsActivity.class.getSimpleName() + ":: ";

    private MoviesAPIService mService;
    private List<ResultsItemReviewsFromMovie> mReviews;
    private List<ResultsItemVideosFromMovie> mVideos;

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

        mService = MoviesAPIServiceBuilder.build();
        mService.getReviewsFromMovie(String.valueOf(movie.getId()), API_KEY)
                .enqueue(new Callback<ResponseReviewsFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseReviewsFromMovie> call, Response<ResponseReviewsFromMovie> response) {
                        if (response.isSuccessful()) {
                            mReviews = response.body().getResults();
                        } else {
                            Log.e(APP_TAG, CLASS_TAG + "reviews response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReviewsFromMovie> call, Throwable t) {
                        Log.e(APP_TAG, CLASS_TAG + "reviews call was onFailure:" + t.getMessage());
                    }
                });

        mService.getVideosFromMovie(String.valueOf(movie.getId()), API_KEY)
                .enqueue(new Callback<ResponseVideosFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseVideosFromMovie> call, Response<ResponseVideosFromMovie> response) {
                        if (response.isSuccessful()) {
                            mVideos = response.body().getResults();
                        } else {
                            Log.e(APP_TAG, CLASS_TAG + "videos response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseVideosFromMovie> call, Throwable t) {
                        Log.e(APP_TAG, CLASS_TAG + "videos call was onFailure:" + t.getMessage());
                    }
                });

    }

}
