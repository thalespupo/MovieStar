package com.tapura.moviestar.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tapura.moviestar.BuildConfig;
import com.tapura.moviestar.R;
import com.tapura.moviestar.model.MoviesBySortResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tapura.moviestar.Constants.APP_TAG;

public class MainActivity extends AppCompatActivity implements Callback<MoviesBySortResponse> {
    private static final String CLASS_TAG = MainActivity.class.getSimpleName() + ":: ";

    private RecyclerView mMoviesGrid;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies_grid);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mAdapter = new MovieAdapter(this);

        mMoviesGrid.setAdapter(mAdapter);
        mMoviesGrid.setLayoutManager(layoutManager);
        mMoviesGrid.setHasFixedSize(true);

        MoviesAPIService service = MoviesAPIServiceBuilder.build();

        Log.d(APP_TAG, CLASS_TAG + "Calling API using Retrofit");
        service.getMovies("popular", BuildConfig.API_KEY).enqueue(this);
    }

    @Override
    public void onResponse(Call<MoviesBySortResponse> call, Response<MoviesBySortResponse> response) {
        if (response.isSuccessful()) {
            mAdapter.setMovieList(response.body().getResults());
            Log.d(APP_TAG, CLASS_TAG + "Movie list set in adapter");
        } else {
            // TODO else for "response.isSuccessful"
            Log.d(APP_TAG, CLASS_TAG + "onResponse failed");
        }
    }

    @Override
    public void onFailure(Call<MoviesBySortResponse> call, Throwable t) {
        Log.d(APP_TAG, CLASS_TAG + "onFailure");
    }
}
