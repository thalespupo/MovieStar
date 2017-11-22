package com.tapura.moviestar.details;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tapura.moviestar.R;

public class ContentHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView tvHeader;

    public ContentHeaderViewHolder(View itemView) {
        super(itemView);
        tvHeader = itemView.findViewById(R.id.text_view_header);

    }

    public TextView getTvHeader() {
        return tvHeader;
    }

    public void setTvHeader(TextView tvHeader) {
        this.tvHeader = tvHeader;
    }

}
