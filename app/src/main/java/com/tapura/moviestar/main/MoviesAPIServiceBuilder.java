package com.tapura.moviestar.main;

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
