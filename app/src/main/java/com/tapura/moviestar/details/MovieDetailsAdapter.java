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

import java.util.ArrayList;
import java.util.List;

import static com.tapura.moviestar.Constants.APP_TAG;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String CLASS_TAG = MovieDetailsAdapter.class.getSimpleName() + ":: ";
    private static final int DETAILS = 0;
    public static final int TITLE_VIDEO = 1;
    private static final int VIDEO = 2;
    public static final int TITLE_REVIEW = 3;
    private static final int REVIEW = 4;

    private Movie mMovie;
    private List<Video> mVideos = new ArrayList<>();
    private List<Review> mReviews = new ArrayList<>();

    private Context mContext;


    public MovieDetailsAdapter(Context context) {
        mContext = context;
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        notifyDataSetChanged();
    }

    public void setVideos(List<Video> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }

    public void setReviews(List<Review> review) {
        this.mReviews = review;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 1 + 1 + mVideos.size() + 1 + mReviews.size(); // movie + title video + videos + title review + reviews
    }

    @Override
    public int getItemViewType(int position) {
        int offset = 0;

        if (position == offset) {
            return DETAILS;
        }
        offset++;

        if (position == offset) {
            return TITLE_VIDEO;
        }
        offset++;

        if (position < offset + mVideos.size()) {
            return VIDEO;
        }
        offset += mVideos.size();

        if (position == offset) {
            return TITLE_REVIEW;
        }
        offset++;

        if (position < offset + mReviews.size()) {
            return REVIEW;
        }
        offset += mReviews.size();

        return -1;
    }

    private int getRelativePosition(int position) {
        int offset = 0;

        if (position == offset) {
            return -1;
        }
        offset++;

        if (position == offset) {
            return -1;
        }
        offset++;

        if (position < offset + mVideos.size()) {
            return position - offset;
        }
        offset += mVideos.size();

        if (position == offset) {
            return -1;
        }
        offset++;

        if (position < offset + mReviews.size()) {
            return position - offset;
        }
        offset += mReviews.size();

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
            case TITLE_VIDEO:
            case TITLE_REVIEW:
                View vTitleReview = inflater.inflate(R.layout.movie_header, parent, false);
                viewHolder = new ContentHeaderViewHolder(vTitleReview, viewType);
                break;
            default:
                Log.wtf(APP_TAG, CLASS_TAG + "on create using default view holder... WHY???");
                return null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int relativePosition = getRelativePosition(position);
        switch (holder.getItemViewType()) {
            case DETAILS:
                DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;
                onBindDetails(detailsViewHolder, relativePosition);
                break;
            case REVIEW:
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) holder;
                onBindReview(reviewViewHolder, relativePosition);
                break;
            case VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
                onBindVideo(videoViewHolder, relativePosition);
                break;
            case TITLE_VIDEO:
          /*  case TITLE_REVIEW:
                ContentHeaderViewHolder contentHeaderViewHolder = (ContentHeaderViewHolder) holder;
                onBindHeader(contentHeaderViewHolder, relativePosition);
                break;*/
            default:
                Log.wtf(APP_TAG, CLASS_TAG + "on bind using default view holder... WHY???");
        }
    }

    private void onBindDetails(DetailsViewHolder detailsViewHolder, int position) {
        Picasso.with(mContext).load(mMovie.getUriBackdropPath()).into(detailsViewHolder.getIvPoster());
        detailsViewHolder.getTvOriginalTitle().setText(mMovie.getOriginalTitle());
        detailsViewHolder.getTvOverview().setText(mMovie.getOverview());
        detailsViewHolder.getRbVoteAverage().setRating(mMovie.getVoteAverageFloat());
        detailsViewHolder.getTvReleaseDate().setText(mMovie.getReleaseDate());
    }

    private void onBindReview(ReviewViewHolder reviewViewHolder, int position) {
        Review r = mReviews.get(position);
        reviewViewHolder.getTvAuthor().setText(r.getAuthor());
        reviewViewHolder.getTvContent().setText(r.getContent());
    }

    private void onBindVideo(VideoViewHolder videoViewHolder, int position) {
        Video v = mVideos.get(position);
        videoViewHolder.getTvVideoLabel().setText(v.getName());
    }

    private void onBindHeader(ContentHeaderViewHolder contentHeaderViewHolder, int position) {
        //contentHeaderViewHolder.getTvHeader().setText(header);
    }

    public List<Video> getVideos() {
        return mVideos;
    }

    public List<Review> getReviews() {
        return mReviews;
    }
}
