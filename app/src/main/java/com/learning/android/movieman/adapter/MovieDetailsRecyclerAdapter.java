package com.learning.android.movieman.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.Cast;
import com.learning.android.movieman.util.UrlEndpoints;

import java.util.List;

/**
 * Created by adrianrusu on 4/6/15.
 */
public class MovieDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<Cast> castList;
    private View headerView;
    private LayoutInflater layoutInflater;

    public MovieDetailsRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public MovieDetailsRecyclerAdapter(Context context, View headerView) {
        this.headerView = headerView;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(headerView);
        } else {
            return new ItemViewHolder(layoutInflater.inflate(R.layout.recycler_cast_item, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemViewHolder) {
            Cast cast = castList.get(i - 1);

            ((ItemViewHolder) viewHolder).textName.setText(cast.getName());
            ((ItemViewHolder) viewHolder).textCharacter.setText(cast.getCharacter());
            if (cast.getProfilePath() != null) {

                StringBuilder posterUrl = new StringBuilder(UrlEndpoints.URL_API_IMAGES);
                posterUrl.append(UrlEndpoints.URL_POSTER_SMALL).append(cast.getProfilePath());

                ImageLoader imageLoader = Repository.getInstance().getImageLoader();
                imageLoader.get(posterUrl.toString(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                            ((ItemViewHolder) viewHolder).imageProfile.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (headerView == null) {
            if (castList == null) {
                return 0;
            } else {
                return castList.size();
            }
        } else {
            if (castList == null) {
                return 1;
            } else {
                return castList.size() + 1;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public View getHeaderView() {
        return headerView;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProfile;
        private TextView textName;
        private TextView textCharacter;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageProfile = (ImageView) itemView.findViewById(R.id.image_cast_profile);
            textName = (TextView) itemView.findViewById(R.id.text_cast_name);
            textCharacter = (TextView) itemView.findViewById(R.id.text_cast_character);
        }
    }
}