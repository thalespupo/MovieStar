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
    private List<Video> mVideos;
    private List<Review> mReviews;

    private boolean isVideosLoaded = false;
    private boolean isReviewsLoaded = false;



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

        mVideos = new ArrayList<>();
        mReviews = new ArrayList<>();

        mAdapter.setItems(mItems);

        mService = MoviesAPIServiceBuilder.build();

        mService.getVideosFromMovie(String.valueOf(movie.getId()), API_KEY)
                .enqueue(new Callback<ResponseVideosFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseVideosFromMovie> call, Response<ResponseVideosFromMovie> response) {
                        isVideosLoaded = true;
                        if (response.isSuccessful()) {
                            mVideos = response.body().getResults();
                        } else {
                            Log.e(APP_TAG, CLASS_TAG + "videos response failed");;
                        }
                        setItemsInAdapter();
                    }

                    @Override
                    public void onFailure(Call<ResponseVideosFromMovie> call, Throwable t) {
                        isVideosLoaded = true;
                        Log.e(APP_TAG, CLASS_TAG + "videos call was onFailure:" + t.getMessage());

                        setItemsInAdapter();
                    }
                });

        mService.getReviewsFromMovie(String.valueOf(movie.getId()), API_KEY)
                .enqueue(new Callback<ResponseReviewsFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseReviewsFromMovie> call, Response<ResponseReviewsFromMovie> response) {
                        isReviewsLoaded = true;
                        if (response.isSuccessful()) {
                            mReviews = response.body().getResults();
                        } else {
                            Log.e(APP_TAG, CLASS_TAG + "reviews response failed");
                        }
                        setItemsInAdapter();
                    }

                    @Override
                    public void onFailure(Call<ResponseReviewsFromMovie> call, Throwable t) {
                        isReviewsLoaded = true;
                        Log.e(APP_TAG, CLASS_TAG + "reviews call was onFailure:" + t.getMessage());

                        setItemsInAdapter();
                    }
                });

    }

    private void setItemsInAdapter() {
        if (isVideosLoaded && isReviewsLoaded) {
            mItems.add(getString(R.string.videos_header));
            for (Video v: mVideos){
                mItems.add(v);
            }

            mItems.add(getString(R.string.review_header));
            for (Review r : mReviews) {
                mItems.add(r);
            }

            mAdapter.setItems(mItems);
        }
    }
}
