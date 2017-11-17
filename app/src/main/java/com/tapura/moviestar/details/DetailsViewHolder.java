package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tapura.moviestar.R;

public class DetailsViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivPoster;
    private TextView tvOriginalTitle;
    private TextView tvOverview;
    private RatingBar rbVoteAverage;
    private TextView tvReleaseDate;

    public DetailsViewHolder(View itemView) {
        super(itemView);
        ivPoster = itemView.findViewById(R.id.image_view_poster);
        tvOriginalTitle = itemView.findViewById(R.id.text_view_original_title);
        tvOverview = itemView.findViewById(R.id.text_view_overview);
        rbVoteAverage = itemView.findViewById(R.id.rating_bar_vote_average);
        tvReleaseDate = itemView.findViewById(R.id.text_view_release_date);
    }

    public ImageView getIvPoster() {
        return ivPoster;
    }

    public void setIvPoster(ImageView ivPoster) {
        this.ivPoster = ivPoster;
    }

    public TextView getTvOriginalTitle() {
        return tvOriginalTitle;
    }

    public void setTvOriginalTitle(TextView tvOriginalTitle) {
        this.tvOriginalTitle = tvOriginalTitle;
    }

    public TextView getTvOverview() {
        return tvOverview;
    }

    public void setTvOverview(TextView tvOverview) {
        this.tvOverview = tvOverview;
    }

    public RatingBar getRbVoteAverage() {
        return rbVoteAverage;
    }

    public void setRbVoteAverage(RatingBar rbVoteAverage) {
        this.rbVoteAverage = rbVoteAverage;
    }

    public TextView getTvReleaseDate() {
        return tvReleaseDate;
    }

    public void setTvReleaseDate(TextView tvReleaseDate) {
        this.tvReleaseDate = tvReleaseDate;
    }
}
