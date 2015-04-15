package com.learning.android.movieman.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.learning.android.movieman.model.YoutubeTrailer;
import com.learning.android.movieman.util.UrlEndpoints;

import java.util.List;

/**
 * Created by adrianrusu on 4/6/15.
 */
public class MovieDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_CAST_ITEM = 1;
    private static final int VIEW_TYPE_TRAILER_HEADER = 2;
    private static final int VIEW_TYPE_TRAILER_ITEM = 3;

    private List<Cast> castList;
    private List<YoutubeTrailer> trailers;
    private View headerView;
    private LayoutInflater layoutInflater;
    private Context context;

    public MovieDetailsRecyclerAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public MovieDetailsRecyclerAdapter(Context context, View headerView) {
        this.headerView = headerView;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(headerView);
        } else if (viewType == VIEW_TYPE_CAST_ITEM) {
            return new CastItemViewHolder(layoutInflater.inflate(R.layout.recycler_cast_item, viewGroup, false));
        } else if (viewType == VIEW_TYPE_TRAILER_HEADER) {
            return new HeaderViewHolder(layoutInflater.inflate(R.layout.recycler_trailer_header, viewGroup, false));
        } else {
            return new TrailerItemViewHolder(layoutInflater.inflate(R.layout.recycler_trailer_item, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CastItemViewHolder) {
            Cast cast = castList.get(position - 1);

            ((CastItemViewHolder) viewHolder).textName.setText(cast.getName());
            ((CastItemViewHolder) viewHolder).textCharacter.setText(cast.getCharacter());
            if (cast.getProfilePath() != null) {

                StringBuilder posterUrl = new StringBuilder(UrlEndpoints.URL_API_IMAGES);
                posterUrl.append(UrlEndpoints.URL_POSTER_SMALL).append(cast.getProfilePath());

                ImageLoader imageLoader = Repository.getInstance().getImageLoader();
                imageLoader.get(posterUrl.toString(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                            ((CastItemViewHolder) viewHolder).imageProfile.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        } else if (viewHolder instanceof TrailerItemViewHolder) {
            int index = position - 1;
            if (headerView != null) {
                index -= 1;
            }
            if (castList != null) {
                index -= castList.size();
            }
            final YoutubeTrailer trailer = trailers.get(index);

            ((TrailerItemViewHolder) viewHolder).textTrailerName.setText(trailer.getName());
            if (trailer.getSource() != null) {
                StringBuilder thumbnailUrl = new StringBuilder("http://img.youtube.com/vi/");
                thumbnailUrl.append(trailer.getSource()).append("/hqdefault.jpg");

                ImageLoader imageLoader = Repository.getInstance().getImageLoader();
                imageLoader.get(thumbnailUrl.toString(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                            ImageView trailerThumbnail = ((TrailerItemViewHolder) viewHolder).imageThumbnail;
                            trailerThumbnail.setImageBitmap(bitmap);
                            trailerThumbnail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailer.getSource()));
                                    context.startActivity(intent);
                                }
                            });
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
        int size = 0;
        if (headerView != null) {
            size = 1;
        }
        if (castList != null) {
            size += castList.size();
        }
        if (trailers != null) {
            size += trailers.size() + 1;
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return VIEW_TYPE_HEADER;
        } else {
            int hasHeader = 0;
            if (headerView != null) {
                hasHeader = 1;
            }
            if (position < castList.size() + hasHeader) {
                return VIEW_TYPE_CAST_ITEM;
            } else if (position == castList.size() + hasHeader) {
                return VIEW_TYPE_TRAILER_HEADER;
            } else {
                return VIEW_TYPE_TRAILER_ITEM;
            }
        }
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public void setTrailers(List<YoutubeTrailer> trailers) {
        this.trailers = trailers;
    }

    public View getHeaderView() {
        return headerView;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    class CastItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProfile;
        private TextView textName;
        private TextView textCharacter;

        public CastItemViewHolder(View itemView) {
            super(itemView);
            imageProfile = (ImageView) itemView.findViewById(R.id.image_cast_profile);
            textName = (TextView) itemView.findViewById(R.id.text_cast_name);
            textCharacter = (TextView) itemView.findViewById(R.id.text_cast_character);
        }
    }

    class TrailerItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageThumbnail;
        private TextView textTrailerName;

        public TrailerItemViewHolder(View itemView) {
            super(itemView);
            imageThumbnail = (ImageView) itemView.findViewById(R.id.image_trailer);
            textTrailerName = (TextView) itemView.findViewById(R.id.text_trailer_name);
        }
    }
}