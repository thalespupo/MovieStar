package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    private TextView mTvVideoLabel;
    private String key;

    public VideoViewHolder(View itemView) {
        super(itemView);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
