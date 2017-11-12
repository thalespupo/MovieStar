package com.tapura.moviestar.api;

import com.tapura.moviestar.model.ResponseMoviesBySort;
import com.tapura.moviestar.model.ResponseReviewsFromMovie;
import com.tapura.moviestar.model.ResponseVideosFromMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesAPIService {
    @GET("movie/{sort}")
    Call<ResponseMoviesBySort> getMoviesBySort(
            @Path("sort") String sort,
            @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ResponseVideosFromMovie> getVideosFromMovie(
            @Path("id") String id,
            @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ResponseReviewsFromMovie> getReviewsFromMovie(
            @Path("id") String id,
            @Query("api_key") String apiKey);
}
