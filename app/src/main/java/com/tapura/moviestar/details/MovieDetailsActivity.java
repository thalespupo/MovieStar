package com.tapura.moviestar.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tapura.moviestar.R;
import com.tapura.moviestar.api.MoviesAPIService;
import com.tapura.moviestar.api.MoviesAPIServiceBuilder;
import com.tapura.moviestar.model.ResponseReviewsFromMovie;
import com.tapura.moviestar.model.ResponseVideosFromMovie;
import com.tapura.moviestar.model.Movie;
import com.tapura.moviestar.model.Review;
import com.tapura.moviestar.model.Video;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.moviestar.BuildConfig.API_KEY;
import static com.tapura.moviestar.Constants.APP_TAG;
import static com.tapura.moviestar.Constants.KEY_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String CLASS_TAG = MovieDetailsActivity.class.getSimpleName() + ":: ";

    private RecyclerView mRecyclerView;
    private MovieDetailsAdapter mAdapter;
    private MoviesAPIService mService;
    private List<Object> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mRecyclerView = findViewById(R.id.recycler_view_movie_details);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra(KEY_MOVIE));
        setTitle(movie.getTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mAdapter = new MovieDetailsAdapter(this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mItems = new ArrayList<>();
        mItems.add(movie);

        mAdapter.setItems(mItems);

        mService = MoviesAPIServiceBuilder.build();
        mService.getReviewsFromMovie(String.valueOf(movie.getId()), API_KEY)
                .enqueue(new Callback<ResponseReviewsFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseReviewsFromMovie> call, Response<ResponseReviewsFromMovie> response) {
                        if (response.isSuccessful()) {
                            for (Review review :response.body().getResults()) {
                                mItems.add(review);
                            }
                            mAdapter.setItems(mItems);
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
                            for (Video video :response.body().getResults()) {
                                mItems.add(video);
                            }
                            mAdapter.setItems(mItems);
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
