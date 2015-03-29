package com.learning.android.movieman.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.Movie;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONObject;

public class MovieDetailsActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private Long movieId;
    private Bitmap lowResBackdrop;
    private int vibrantRgbColor;
    private int vibrantTitleTextColor;

    private Movie movie;

    private Toolbar toolbar;
    private ImageView imageBackdrop;
    private View overlayView;
    private ObservableScrollView scrollView;
    private TextView textTitle;
    private TextView textOverview;

    private int actionBarSize;
    private int flexibleSpaceShowFabOffset;
    private int flexibleSpaceImageHeight;
    private int fabMargin;
    private int toolbarColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getIntentExtras();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        imageBackdrop = (ImageView) findViewById(R.id.image_movie_detail_header);
        overlayView = findViewById(R.id.overlay);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollview_movie_details);
        scrollView.setScrollViewCallbacks(this);
        textTitle = (TextView) findViewById(R.id.text_movie_title);
        textOverview = (TextView) findViewById(R.id.text_movie_overview);

        flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height) - getResources().getDimensionPixelSize(R.dimen.app_bar_top_padding);
        actionBarSize = getActionBarSize();
        toolbarColor = getResources().getColor(R.color.primaryColor);

        if (lowResBackdrop != null) {
            imageBackdrop.setImageBitmap(lowResBackdrop);
        }
        if (vibrantRgbColor != 0) {
            overlayView.setBackgroundColor(vibrantRgbColor);
            toolbarColor = vibrantRgbColor;
        }
        if (vibrantTitleTextColor != 0) {
            textTitle.setTextColor(vibrantTitleTextColor);
        }
        setTitle(null);

        ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
            @Override
            public void run() {
                onScrollChanged(0, false, false);
            }
        });

        sendApiRequest();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - actionBarSize;
        int minOverlayTransitionY = actionBarSize - overlayView.getHeight() + (int) getResources().getDimension(R.dimen.app_bar_top_padding);
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imageBackdrop, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(textTitle, 0);
        ViewHelper.setPivotY(textTitle, 0);
        ViewHelper.setScaleX(textTitle, scale);
        ViewHelper.setScaleY(textTitle, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (flexibleSpaceImageHeight - textTitle.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        titleTranslationY = Math.max(0, titleTranslationY);
        ViewHelper.setTranslationY(textTitle, titleTranslationY);

        // Change alpha of toolbar background
        if (-scrollY + flexibleSpaceImageHeight <= actionBarSize) {
            toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, toolbarColor));
        } else {
            toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, toolbarColor));
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        movieId = intent.getExtras().getLong("id");
        if (intent.hasExtra("backdrop")) {
            lowResBackdrop = intent.getParcelableExtra("backdrop");
        }
        if (intent.hasExtra("vibrantRgbColor")) {
            vibrantRgbColor = intent.getExtras().getInt("vibrantRgbColor");
            if (intent.hasExtra("vibrantTitleTextColor")) {
                vibrantTitleTextColor = intent.getExtras().getInt("vibrantTitleTextColor");
            }
        }
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private void sendApiRequest() {
        RequestQueue requestQueue = Repository.getInstance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(), (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError(error);
            }
        });
        requestQueue.add(request);
    }

    private void handleError(VolleyError error) {
    }

    private void handleResponse(JSONObject response) {
        movie = JsonUtils.parseMovieJsonResponse(response);

        if (movie != null) {
            if (movie.getBackdropPath() != null) {
                sendApiBackdropRequest();
            }
            textTitle.setText(movie.getTitle());
            textOverview.setText(movie.getOverview());
            // just testing
            StringBuilder sb = new StringBuilder(movie.getOverview());
            for (int i = 0; i < 10; i++)
                sb.append(movie.getOverview());
            // end of just testing
            textOverview.setText(sb.toString());
        }
    }

    private void sendApiBackdropRequest() {
        ImageLoader imageLoader = Repository.getInstance().getImageLoader();
        imageLoader.get(getBackdropRequestUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    imageBackdrop.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private String getRequestUrl() {
        return UrlEndpoints.URL_API_HOME + "movie" + "/" + movieId.toString() + UrlEndpoints.URL_PARAM_API_KEY;
    }

    private String getBackdropRequestUrl() {
        return UrlEndpoints.URL_API_IMAGES + UrlEndpoints.URL_BACKDROP_LARGE + movie.getBackdropPath();
    }
}
