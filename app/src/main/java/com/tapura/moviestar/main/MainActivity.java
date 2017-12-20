package com.tapura.moviestar.main;

import android.content.Intent;
import android.content.res.Configuration;
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
import com.tapura.moviestar.data.FavouriteDbUtils;
import com.tapura.moviestar.details.MovieDetailsActivity;
import com.tapura.moviestar.model.Movie;
import com.tapura.moviestar.model.ResponseMoviesBySort;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.moviestar.Constants.APP_TAG;
import static com.tapura.moviestar.Constants.KEY_MOVIE;
import static com.tapura.moviestar.data.FavouriteContentProvider.FAVOURITES;

public class MainActivity extends AppCompatActivity implements Callback<ResponseMoviesBySort>, MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName() + ":: ";

    private static final String KEY_SORT = "sort";
    private static final String KEY_MOVIES_LIST = "movies_list";

    private RecyclerView mMoviesGrid;
    private MovieAdapter mAdapter;
    private MoviesAPIService mService;
    private Loader<List<Movie>> mLoader;

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

        mService = MoviesAPIServiceBuilder.build();

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
            Log.d(APP_TAG, CLASS_TAG + "Movie list set in adapter");
        } else {
            // TODO else for "response.isSuccessful"
            Log.d(APP_TAG, CLASS_TAG + "onResponse failed");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_SORT, mCurrentMovieSort.name());
        outState.putParcelable(KEY_MOVIES_LIST, Parcels.wrap(mAdapter.getMovieList()));
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
        Log.d(APP_TAG, CLASS_TAG + "DB call via AsyncLoader");
        mCurrentMovieSort = MovieSort.FAVOURITE;
        mLoader = getSupportLoaderManager().initLoader(FAVOURITES, null, this);
    }

    private void handleRotate(Bundle savedInstanceState) {
        mCurrentMovieSort = MovieSort.valueOf(savedInstanceState.getString(KEY_SORT));
        List<Movie> savedList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIES_LIST));
        if (savedList == null) {
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
            if (mCurrentMovieSort != MovieSort.FAVOURITE) { // Always refresh for local database
                mAdapter.setMovieList(savedList);
            } else {
                getFavouriteMovies(null);
            }
        }
    }

    @Override
    public void onClick(int pos) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Movie movie = mAdapter.get(pos);
        intent.putExtra(KEY_MOVIE, Parcels.wrap(movie));
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
                return FavouriteDbUtils.getFavouriteMovies(MainActivity.this);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if (mCurrentMovieSort == MovieSort.FAVOURITE) {
            mAdapter.setMovieList(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}