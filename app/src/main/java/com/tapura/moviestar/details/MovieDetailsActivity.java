package com.tapura.moviestar.details;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tapura.moviestar.R;
import com.tapura.moviestar.api.MoviesAPIService;
import com.tapura.moviestar.api.MoviesAPIServiceBuilder;
import com.tapura.moviestar.model.Movie;
import com.tapura.moviestar.model.ResponseReviewsFromMovie;
import com.tapura.moviestar.model.ResponseVideosFromMovie;
import com.tapura.moviestar.model.Review;
import com.tapura.moviestar.model.Video;

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
    private static final String KEY_ITEMS_LIST = "list_items";
    private static final String KEY_IS_FAVOURITE = "is_favourite";
    private static final String KEY_VIDEOS = "videos";
    private static final String KEY_REVIEWS = "reviews";

    private RecyclerView mRecyclerView;
    private MovieDetailsAdapter mAdapter;
    private MoviesAPIService mService;
    private Movie mMovie;

    private boolean isFavourite = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mRecyclerView = findViewById(R.id.recycler_view_movie_details);

        mMovie = Parcels.unwrap(getIntent().getParcelableExtra(KEY_MOVIE));
        setTitle(mMovie.getTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mAdapter = new MovieDetailsAdapter(this);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter.setMovie(mMovie);

        mService = MoviesAPIServiceBuilder.build();

        if (savedInstanceState != null) {
            loadFrom(savedInstanceState);
        } else {
            requestVideosAndReviews();
        }
    }

    private void requestVideosAndReviews() {
        mService.getVideosFromMovie(String.valueOf(mMovie.getId()), API_KEY)
                .enqueue(new Callback<ResponseVideosFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseVideosFromMovie> call, Response<ResponseVideosFromMovie> response) {
                        if (response.isSuccessful()) {
                            mAdapter.setVideos(response.body().getResults());
                        } else {
                            Log.e(APP_TAG, CLASS_TAG + "videos response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseVideosFromMovie> call, Throwable t) {
                        Log.e(APP_TAG, CLASS_TAG + "videos call was onFailure:" + t.getMessage());
                    }
                });

        mService.getReviewsFromMovie(String.valueOf(mMovie.getId()), API_KEY)
                .enqueue(new Callback<ResponseReviewsFromMovie>() {
                    @Override
                    public void onResponse(Call<ResponseReviewsFromMovie> call, Response<ResponseReviewsFromMovie> response) {
                        if (response.isSuccessful()) {
                            mAdapter.setReviews(response.body().getResults());
                        } else {
                            Log.e(APP_TAG, CLASS_TAG + "reviews response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseReviewsFromMovie> call, Throwable t) {
                        Log.e(APP_TAG, CLASS_TAG + "reviews call was onFailure:" + t.getMessage());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        setFavourite(menu.getItem(0), isFavourite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_mark_favourite) {
            setFavourite(item, !isFavourite);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFavourite(MenuItem item, boolean isFav) {

        Drawable favourite = getResources().getDrawable(R.drawable.ic_star_gold, null);
        Drawable notFavourite = getResources().getDrawable(R.drawable.ic_star_gold_border, null);

        if (isFav) {
            item.setIcon(favourite);
            isFavourite = true;
        } else {
            item.setIcon(notFavourite);
            isFavourite = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_VIDEOS, Parcels.wrap(mAdapter.getVideos()));
        outState.putParcelable(KEY_REVIEWS, Parcels.wrap(mAdapter.getReviews()));
        outState.putBoolean(KEY_IS_FAVOURITE, isFavourite);
        super.onSaveInstanceState(outState);
    }


    private void loadFrom(Bundle savedInstanceState) {
        mAdapter.setReviews((List<Review>) Parcels.unwrap(savedInstanceState.getParcelable(KEY_REVIEWS)));
        mAdapter.setVideos((List<Video>) Parcels.unwrap(savedInstanceState.getParcelable(KEY_VIDEOS)));
        isFavourite = savedInstanceState.getBoolean(KEY_IS_FAVOURITE);
    }
}
