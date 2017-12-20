package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapura.moviestar.R;
import com.tapura.moviestar.model.Video;

public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView mThumbnail;
    private TextView tvVideoLabel;
    private Video mVideo;

    private MovieDetailsAdapter.MovieDetailsOnClickHandler mCallBack;

    public VideoViewHolder(View itemView, MovieDetailsAdapter.MovieDetailsOnClickHandler callBack) {
        super(itemView);
        mThumbnail = itemView.findViewById(R.id.image_view_video_thumbnail);
        tvVideoLabel = itemView.findViewById(R.id.text_view_video_name);
        mCallBack = callBack;
        itemView.setOnClickListener(this);

    }

    public TextView getTvVideoLabel() {
        return tvVideoLabel;
    }

    public void setTvVideoLabel(TextView tvVideoLabel) {
        this.tvVideoLabel = tvVideoLabel;
    }

    public Video getVideo() {
        return mVideo;
    }

    public void setVideo(Video mVideo) {
        this.mVideo = mVideo;
    }

    @Override
    public void onClick(View v) {
        mCallBack.onClick(mVideo);
    }

    public ImageView getThumbnail() {
        return mThumbnail;
    }
}
