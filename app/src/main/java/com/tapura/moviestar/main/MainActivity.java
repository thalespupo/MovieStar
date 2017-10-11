package com.tapura.moviestar.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.tapura.moviestar.details.MovieDetailsActivity;
import com.tapura.moviestar.model.MoviesBySortResponse;
import com.tapura.moviestar.model.ResultsItem;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.moviestar.Constants.APP_TAG;
import static com.tapura.moviestar.Constants.KEY_MOVIE;

public class MainActivity extends AppCompatActivity implements Callback<MoviesBySortResponse>,MovieAdapter.MovieAdapterOnClickHandler {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName() + ":: ";

    private static final String KEY_SORT = "sort";
    private static final String KEY_MOVIES_LIST = "movies_list";

    private RecyclerView mMoviesGrid;
    private MovieAdapter mAdapter;
    private MoviesAPIService mService;
    private List<ResultsItem> mCache;

    private enum MovieSort {
        POPULAR, HIGHEST_RATED
    }

    private MovieSort mCurrentMovieSort = MovieSort.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGrid = findViewById(R.id.rv_movies_grid);

        GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }

        mAdapter = new MovieAdapter(this, this);

        mMoviesGrid.setAdapter(mAdapter);
        mMoviesGrid.setLayoutManager(layoutManager);
        mMoviesGrid.setHasFixedSize(true);

        mCache = new ArrayList<>();

        mService = MoviesAPIServiceBuilder.build();

        if (savedInstanceState != null) {
            handleRotate(savedInstanceState);
        } else {
            getMoviesByPopular(null);
        }
    }

    @Override
    public void onResponse(Call<MoviesBySortResponse> call, Response<MoviesBySortResponse> response) {
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
    public void onFailure(Call<MoviesBySortResponse> call, Throwable t) {
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
        mService.getMovies("popular", BuildConfig.API_KEY).enqueue(this);
    }

    public void getMoviesByHighestRated(MenuItem item) {
        Log.d(APP_TAG, CLASS_TAG + "Calling MovieDB API using Retrofit");
        mCurrentMovieSort = MovieSort.HIGHEST_RATED;
        mService.getMovies("top_rated", BuildConfig.API_KEY).enqueue(this);
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
            }
        } else {
            mAdapter.setMovieList(mCache);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(int pos) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        ResultsItem movie = mCache.get(pos);
        intent.putExtra(KEY_MOVIE, Parcels.wrap(movie));
        startActivity(intent);
    }
}