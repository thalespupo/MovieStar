package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private TextView tvVideoLabel;
    private String key;

    public VideoViewHolder(View itemView) {
        super(itemView);
        tvVideoLabel = itemView.findViewById(android.R.id.text1);

    }

    public TextView getTvVideoLabel() {
        return tvVideoLabel;
    }

    public void setTvVideoLabel(TextView tvVideoLabel) {
        this.tvVideoLabel = tvVideoLabel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
