package com.tapura.moviestar.details;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tapura.moviestar.R;
import com.tapura.moviestar.model.Movie;
import com.tapura.moviestar.model.Review;
import com.tapura.moviestar.model.Video;

import java.util.List;

import static com.tapura.moviestar.Constants.APP_TAG;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String CLASS_TAG = MovieDetailsAdapter.class.getSimpleName() + ":: ";

    private List<Object> mItems;
    private Context mContext;

    private final int DETAILS = 0;
    private final int VIDEO = 1;
    private final int REVIEW = 2;

    public MovieDetailsAdapter(Context context) {
        mContext = context;
    }

    public void setItems(List<Object> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof Movie) {
            return DETAILS;
        } else if (mItems.get(position) instanceof Video) {
            return VIDEO;
        } else if (mItems.get(position) instanceof Review) {
            return REVIEW;
        }
        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case DETAILS:
                View v1 = inflater.inflate(R.layout.movie_details, parent, false);
                viewHolder = new DetailsViewHolder(v1);
                break;
            case REVIEW:
                View v2 = inflater.inflate(R.layout.movie_review, parent, false);
                viewHolder = new ReviewViewHolder(v2);
                break;
            case VIDEO:
                View v3 = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new VideoViewHolder(v3);
                break;
            default:
                Log.wtf(APP_TAG, CLASS_TAG + "on create using default view holder... WHY???");
                return null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case DETAILS:
                DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;
                onBindDetails(detailsViewHolder, position);
                break;
            case REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                onBindReview(reviewViewHolder, position);
                break;
            case VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                onBindVideo(videoViewHolder, position);
                break;
            default:
                Log.wtf(APP_TAG, CLASS_TAG + "on bind using default view holder... WHY???");
        }
    }


    private void onBindDetails(DetailsViewHolder detailsViewHolder, int position) {
        Movie movie = (Movie) mItems.get(position);

        Picasso.with(mContext).load(movie.getBackdropPath()).into(detailsViewHolder.getIvPoster());
        detailsViewHolder.getTvOriginalTitle().setText(movie.getOriginalTitle());
        detailsViewHolder.getTvOverview().setText(movie.getOverview());
        detailsViewHolder.getRbVoteAverage().setRating(movie.getVoteAverageFloat());
        detailsViewHolder.getTvReleaseDate().setText(movie.getReleaseDate());
    }

    private void onBindReview(ReviewViewHolder reviewViewHolder, int position) {

    }

    private void onBindVideo(VideoViewHolder videoViewHolder, int position) {

    }
}
