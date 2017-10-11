package com.tapura.moviestar.api;

import com.tapura.moviestar.model.MoviesBySortResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesAPIService {
    @GET("movie/{sort}")
    Call<MoviesBySortResponse> getMovies(
            @Path("sort") String sort,
            @Query("api_key") String apiKey);
}
