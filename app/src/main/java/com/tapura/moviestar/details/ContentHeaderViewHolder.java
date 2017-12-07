package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tapura.moviestar.R;

import static com.tapura.moviestar.details.MovieDetailsAdapter.TITLE_REVIEW;
import static com.tapura.moviestar.details.MovieDetailsAdapter.TITLE_VIDEO;

public class ContentHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView tvHeader;

    public ContentHeaderViewHolder(View itemView, int titleType) {
        super(itemView);
        tvHeader = itemView.findViewById(R.id.text_view_header);
        switch (titleType) {
            case TITLE_VIDEO:
                tvHeader.setText(R.string.videos_header);
                break;
            case TITLE_REVIEW:
                tvHeader.setText(R.string.review_header);
                break;
            default:
                // do nothing
        }
    }

    public TextView getTvHeader() {
        return tvHeader;
    }

    public void setTvHeader(TextView tvHeader) {
        this.tvHeader = tvHeader;
    }
}
