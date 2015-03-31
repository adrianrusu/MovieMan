package com.learning.android.movieman.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import com.nineoldandroids.view.ViewPropertyAnimator;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;

public class MovieDetailsActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static DecimalFormat df = new DecimalFormat("#,###,###,##0");

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
    private TextView textTagline;
    private TextView textDuration;
    private TextView textHomepage;
    private TextView textBudget;
    private TextView textRevenue;
    private TextView textOverview;
    private TextView textGenres;
    private View fab;

    private int actionBarSize;
    private int flexibleSpaceImageHeight;
    private int toolbarColor;
    private int fabMargin;
    private boolean fabIsShown;


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
        textTagline = (TextView) findViewById(R.id.text_movie_tagline);
        textBudget = (TextView) findViewById(R.id.text_movie_budget);
        textRevenue = (TextView) findViewById(R.id.text_movie_revenue);
        textDuration = (TextView) findViewById(R.id.text_movie_duration);
        textHomepage = (TextView) findViewById(R.id.text_movie_homepage);
        textOverview = (TextView) findViewById(R.id.text_movie_overview);
        textGenres = (TextView) findViewById(R.id.text_movie_genres);
        fab = findViewById(R.id.floating_actions_menu);
        ((FloatingActionsMenu) fab).setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                scrollView.smoothScrollTo(0, 0);
            }

            @Override
            public void onMenuCollapsed() {

            }
        });
        findViewById(R.id.floating_action_fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MovieDetailsActivity.this, "Added to favourites", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.floating_action_watch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MovieDetailsActivity.this, "Added to watchlist", Toast.LENGTH_SHORT).show();
            }
        });
        fabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        ViewHelper.setScaleX(fab, 0);
        ViewHelper.setScaleY(fab, 0);

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
//        if (vibrantTitleTextColor != 0) {
//            textTitle.setTextColor(vibrantTitleTextColor);
//        }
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
        ViewHelper.setTranslationY(imageBackdrop, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

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

        // Translate FAB
        float fabTranslationY = ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0);
        ViewHelper.setTranslationX(fab, overlayView.getWidth() - fabMargin - fab.getWidth());
        ViewHelper.setTranslationY(fab, fabTranslationY);

        // Show/hide FAB
        if (Math.round(scale * 100) / 100d < 1 + MAX_TEXT_SCALE_DELTA) {
            hideFab();
        } else {
            showFab();
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

    private void showFab() {
        if (!fabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel();
            ViewPropertyAnimator.animate(fab).scaleX(1).scaleY(1).setDuration(200).start();
            fabIsShown = true;
        }
    }

    private void hideFab() {
        if (fabIsShown) {
            ViewPropertyAnimator.animate(fab).cancel();
            ViewPropertyAnimator.animate(fab).scaleX(0).scaleY(0).setDuration(200).start();
            fabIsShown = false;
        }
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
            StringBuilder sb = new StringBuilder(movie.getTitle());
            if (movie.getReleaseDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(movie.getReleaseDate());
                sb.append(" (").append(Integer.toString(cal.get(Calendar.YEAR))).append(")");
            }
            if (movie.getTagline() != null && !movie.getTagline().isEmpty()) {
                textTagline.setText(movie.getTagline());
            }
            textTitle.setText(sb.toString());
            if (movie.getRuntime() != null) {
                textDuration.setText(movie.getRuntime().toString() + " min");
            }
            if (movie.getHomepage() != null && !movie.getHomepage().isEmpty()) {
                textHomepage.setText(Html.fromHtml("<a href=\"" + movie.getHomepage() + "\">Homepage</a>"));
                textHomepage.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (movie.getBudget() != null && movie.getBudget().compareTo(BigDecimal.ZERO) == 1) {
                textBudget.setText("$" + df.format(movie.getBudget()));
            }
            if (movie.getRevenue() != null && movie.getRevenue().compareTo(BigDecimal.ZERO) == 1) {
                textRevenue.setText("$" + df.format(movie.getRevenue()));
            }
            textOverview.setText(movie.getOverview());
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                sb = new StringBuilder();
                for (int i = 0; i < movie.getGenres().size(); i++) {
                    sb.append(movie.getGenres().get(i).getName());
                    if (i != (movie.getGenres().size() - 1))
                        sb.append(", ");
                }
                textGenres.setText(sb.toString());
            }
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
