package com.tapura.moviestar.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tapura.moviestar.BuildConfig;
import com.tapura.moviestar.R;
import com.tapura.moviestar.api.MoviesAPIService;
import com.tapura.moviestar.api.MoviesAPIServiceBuilder;
import com.tapura.moviestar.data.sql.FavouriteMoviesContract;
import com.tapura.moviestar.details.MovieDetailsActivity;
import com.tapura.moviestar.model.Movie;
import com.tapura.moviestar.model.ResponseMoviesBySort;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.moviestar.Constants.APP_TAG;
import static com.tapura.moviestar.Constants.KEY_FAVOURITE;
import static com.tapura.moviestar.Constants.KEY_MOVIE;
import static com.tapura.moviestar.data.FavouriteContentProvider.FAVOURITE_WITH_ID;

public class MainActivity extends AppCompatActivity implements Callback<ResponseMoviesBySort>, MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName() + ":: ";

    private static final String KEY_SORT = "sort";
    private static final String KEY_MOVIES_LIST = "movies_list";

    private RecyclerView mMoviesGrid;
    private MovieAdapter mAdapter;
    private MoviesAPIService mService;
    private List<Movie> mCache;
    private List<Movie> mFavourites;

    private enum MovieSort {
        POPULAR, HIGHEST_RATED, FAVOURITE
    }

    private MovieSort mCurrentMovieSort = MovieSort.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGrid = findViewById(R.id.rv_movies_grid);

        GridLayoutManager layoutManager;

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }

        mAdapter = new MovieAdapter(this, this);

        mMoviesGrid.setAdapter(mAdapter);
        mMoviesGrid.setLayoutManager(layoutManager);
        mMoviesGrid.setHasFixedSize(true);

        mCache = new ArrayList<>();
        mFavourites = new ArrayList<>();

        mService = MoviesAPIServiceBuilder.build();

        getSupportLoaderManager().initLoader(FAVOURITE_WITH_ID, null, this);

        if (savedInstanceState != null) {
            handleRotate(savedInstanceState);
        } else {
            getMoviesByPopular(null);
        }
    }

    @Override
    public void onResponse(Call<ResponseMoviesBySort> call, Response<ResponseMoviesBySort> response) {
        if (response.isSuccessful()) {
            mAdapter.setMovieList(response.body().getResults());
            mAdapter.notifyDataSetChanged();
            mCache = response.body().getResults();

            Log.d(APP_TAG, CLASS_TAG + "Movie list set in adapter");
        } else {
            // TODO else for "response.isSuccessful"
            Log.d(APP_TAG, CLASS_TAG + "onResponse failed");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_SORT, mCurrentMovieSort.name());
        outState.putParcelable(KEY_MOVIES_LIST, Parcels.wrap(mCache));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFailure(Call<ResponseMoviesBySort> call, Throwable t) {
        Log.d(APP_TAG, CLASS_TAG + "onFailure");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void getMoviesByPopular(MenuItem item) {
        Log.d(APP_TAG, CLASS_TAG + "Calling MovieDB API using Retrofit");
        mCurrentMovieSort = MovieSort.POPULAR;
        mService.getMoviesBySort("popular", BuildConfig.API_KEY).enqueue(this);
    }

    public void getMoviesByHighestRated(MenuItem item) {
        Log.d(APP_TAG, CLASS_TAG + "Calling MovieDB API using Retrofit");
        mCurrentMovieSort = MovieSort.HIGHEST_RATED;
        mService.getMoviesBySort("top_rated", BuildConfig.API_KEY).enqueue(this);
    }

    public void getFavouriteMovies(MenuItem item) {
        mAdapter.setMovieList(mFavourites);
    }


    private void handleRotate(Bundle savedInstanceState) {
        mCurrentMovieSort = MovieSort.valueOf(savedInstanceState.getString(KEY_SORT));
        mCache = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIES_LIST));
        if (mCache == null) {
            switch (mCurrentMovieSort) {
                case POPULAR:
                    getMoviesByPopular(null);
                    break;
                case HIGHEST_RATED:
                    getMoviesByHighestRated(null);
                    break;
                case FAVOURITE:
                    getFavouriteMovies(null);
            }
        } else {
            mAdapter.setMovieList(mCache);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(int pos) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Movie movie = mCache.get(pos);
        intent.putExtra(KEY_MOVIE, Parcels.wrap(movie));
        if (mCurrentMovieSort.equals(MovieSort.FAVOURITE)) {
            intent.putExtra(KEY_FAVOURITE, true);
        }
        startActivity(intent);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                try {
                    return getMovies(getContentResolver().query(FavouriteMoviesContract.FavouriteEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE));
                } catch (Exception e) {
                    Log.e(APP_TAG, CLASS_TAG + "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            private List<Movie> getMovies(Cursor c) {
                List<Movie> list = new ArrayList<>();
                if (c != null && c.getCount() > 0) {
                    while (c.moveToNext()) {
                        Movie m = new Movie();

                        m.setId(c.getInt(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_ID_MOVIE)));
                        m.setTitle(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_TITLE)));
                        m.setOriginalTitle(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_ORIGINAL_TITLE)));
                        m.setOverview(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW)));
                        m.setPosterPath(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_POSTER)));
                        m.setBackdropPath(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_BACKDROP)));
                        m.setVoteAverage(c.getDouble(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_RATING)));
                        m.setReleaseDate(c.getString(c.getColumnIndexOrThrow(FavouriteMoviesContract.FavouriteEntry.COLUMN_MOVIE_RELEASE_DATE)));

                        list.add(m);
                    }
                }
                if (c != null) {
                    c.close();
                }
                return list;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mFavourites = data;
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}