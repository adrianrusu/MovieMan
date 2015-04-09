package com.learning.android.movieman.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.joooonho.SelectableRoundedImageView;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.MovieSmall;
import com.learning.android.movieman.util.UrlEndpoints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adrianrusu on 3/23/15.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolderMovies> {

    private List<MovieSmall> movies = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private RecyclerViewSelectionListener selectionListener;

    public MovieListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderMovies onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.recycler_movie_item, viewGroup, false);

        CardView cardView = (CardView) view.findViewById(R.id.home_movie_card_view);
        cardView.setPreventCornerOverlap(false);

        return new ViewHolderMovies(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderMovies viewHolderMovies, final int i) {
        MovieSmall movie = movies.get(i);
        viewHolderMovies.movieTitle.setText(movie.getTitle());

        if (movie.getPosterPath() != null) {
            StringBuilder posterUrl = new StringBuilder(UrlEndpoints.URL_API_IMAGES);
            posterUrl.append(UrlEndpoints.URL_POSTER_MEDIUM).append(movie.getPosterPath());

            ImageLoader imageLoader = Repository.getInstance().getImageLoader();
            imageLoader.get(posterUrl.toString(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();
                    if (bitmap != null) {
                        viewHolderMovies.moviePoster.setImageBitmap(bitmap);
                        viewHolderMovies.moviePoster.setVisibility(View.VISIBLE);
                        viewHolderMovies.posterNotAvailable.setVisibility(View.GONE);

                        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        p.addRule(RelativeLayout.BELOW, R.id.movie_list_poster);
                        viewHolderMovies.movieTitle.setLayoutParams(p);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        } else {
            viewHolderMovies.moviePoster.setVisibility(View.GONE);
            viewHolderMovies.posterNotAvailable.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            p.addRule(RelativeLayout.BELOW, R.id.text_poster_not_available);
            viewHolderMovies.movieTitle.setLayoutParams(p);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setElements(List<MovieSmall> elements) {
        this.movies = elements;
        notifyItemRangeChanged(0, elements.size());
    }

    public void setSelectionListener(RecyclerViewSelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public class ViewHolderMovies extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SelectableRoundedImageView moviePoster;
        private TextView movieTitle;
        private TextView posterNotAvailable;

        public ViewHolderMovies(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            moviePoster = (SelectableRoundedImageView) itemView.findViewById(R.id.movie_list_poster);
            movieTitle = (TextView) itemView.findViewById(R.id.text_movie_title);
            posterNotAvailable = (TextView) itemView.findViewById(R.id.text_poster_not_available);
        }

        @Override
        public void onClick(View v) {
            if (selectionListener != null) {
                selectionListener.itemClicked(v, getPosition());
            }
        }
    }

}
