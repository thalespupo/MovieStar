package com.tapura.moviestar.api;

import com.tapura.moviestar.api.MoviesAPIService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesAPIServiceBuilder {

    public static MoviesAPIService build() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MoviesAPIService.class);
    }
}
