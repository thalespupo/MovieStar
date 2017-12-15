package com.tapura.moviestar.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesAPIServiceBuilder {

    public static MoviesAPIService build() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(MoviesAPIService.class);
    }
}
