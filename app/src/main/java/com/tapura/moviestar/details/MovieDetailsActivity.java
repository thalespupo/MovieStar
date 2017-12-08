package com.tapura.moviestar.details;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tapura.moviestar.R;
import com.tapura.moviestar.api.MoviesAPIService;
import com.tapura.moviestar.api.MoviesAPIServiceBuilder;
import com.tapura.moviestar.data.sql.FavouriteMoviesContract;
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
import static com.tapura.moviestar.data.FavouriteContentProvider.FAVOURITE_WITH_ID;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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
    private boolean fromDb;


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

        getSupportLoaderManager().initLoader(FAVOURITE_WITH_ID, null, this);

        if (savedInstanceState != null) {
            loadFrom(savedInstanceState);
        } else {
            isFavourite = getFromDb();
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
        menu.getItem(0).setIcon(getDrawableByBool());
        return true;
    }

    private Drawable getDrawableByBool() {
        if (isFavourite) {
            return getResources().getDrawable(R.drawable.ic_star_gold, null);
        } else {
            return getResources().getDrawable(R.drawable.ic_star_gold_border, null);
        }
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
            insertInDb(mMovie.getId());
        } else {
            item.setIcon(notFavourite);
            isFavourite = false;
            deleteInDb(mMovie.getId());
        }
    }

    private boolean deleteInDb(int id) {
        int deleteRows = getContentResolver()
                .delete(ContentUris.withAppendedId(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI, id), null, null);

        return deleteRows > 0;
    }

    private void insertInDb(int id) {
        ContentValues cv = new ContentValues();

        cv.put(FavouriteMoviesContract.FavouriteEntry.COLUMN_NAME_MOVIE, id);

        Uri uri = null;
        try {
            uri = getContentResolver().insert(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI, cv);
        } catch (android.database.SQLException e) {
            Toast.makeText(getBaseContext(), "Already inserted", Toast.LENGTH_LONG).show();
        }

        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
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

    public boolean getFromDb() {
        //getContentResolver().query()
        return fromDb;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE);
                } catch (Exception e) {
                    Log.e(APP_TAG, CLASS_TAG + "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
