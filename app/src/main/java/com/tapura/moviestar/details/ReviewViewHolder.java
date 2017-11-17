package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tapura.moviestar.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    private TextView tvAuthor;
    private TextView tvContent;
    public ReviewViewHolder(View itemView) {
        super(itemView);
        tvAuthor = itemView.findViewById(R.id.text_view_review_author);
        tvContent = itemView.findViewById(R.id.text_view_review_content);
    }

    public TextView getTvAuthor() {
        return tvAuthor;
    }

    public void setTvAuthor(TextView tvAuthor) {
        this.tvAuthor = tvAuthor;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public void setTvContent(TextView tvContent) {
        this.tvContent = tvContent;
    }
}
