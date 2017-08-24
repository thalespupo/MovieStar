package com.tapura.moviestar.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tapura.moviestar.R;
import com.tapura.moviestar.model.ResultsItem;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String CLASS_TAG = MovieAdapter.class.getSimpleName() + ":: ";

    private List<ResultsItem> mMovieData;
    private Context mContext;

    public MovieAdapter(Context context) {
        mContext = context;

    }

    public void setMovieList(List<ResultsItem> movieList) {
        this.mMovieData = checkNotNull(movieList);
        this.notifyDataSetChanged();
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mMovieImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_tumbnail);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutIdForListItem = R.layout.view_movie_item_layout;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        ResultsItem movie = mMovieData.get(position);
        Picasso.with(mContext).load(movie.getPosterPath()).placeholder(R.drawable.movie_poster_placeholder).into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

}
