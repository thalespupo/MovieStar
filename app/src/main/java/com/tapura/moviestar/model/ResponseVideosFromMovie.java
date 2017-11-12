package com.tapura.moviestar.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseVideosFromMovie {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<ResultsItemVideosFromMovie> results;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResults(List<ResultsItemVideosFromMovie> results) {
        this.results = results;
    }

    public List<ResultsItemVideosFromMovie> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return
                "ResponseVideosFromMovie{" +
                        "id = '" + id + '\'' +
                        ",results = '" + results + '\'' +
                        "}";
    }
}