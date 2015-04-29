package com.learning.android.movieman.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.Crew;
import com.learning.android.movieman.model.Movie;
import com.learning.android.movieman.model.YoutubeTrailer;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MovieDetailsActivity extends MovieDetailsBaseActivity {

    public static final int SOURCE_MAIN_ACTIVITY = 1;
    public static final int SOURCE_SEARCH_ACTIVITY = 2;
    public static final int SOURCE_WATCHLIST_ACTIVITY = 3;
    public static final int SOURCE_FAVORITES_ACTIVITY = 4;
    public static final int SOURCE_MOVIES_LISTS = 5;

    private static DecimalFormat df = new DecimalFormat("#,###,###,##0");

    private Long movieId;
    private int source;
    private Bitmap lowResBackdrop;
    private int vibrantRgbColor;
    private int vibrantTitleTextColor;

    private Movie movie;

    private TextView textTagline;
    private TextView textDuration;
    private TextView textHomepage;
    private TextView textBudget;
    private TextView textRevenue;
    private TextView textStatus;
    private TextView textDirectedBy;
    private TextView textGenres;
    private TextView textOverview;
    private TextView textProductionCompanies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentExtras();

        View adapterHeader = movieDetailsAdapter.getHeaderView();
        textTagline = (TextView) adapterHeader.findViewById(R.id.text_movie_tagline);
        textBudget = (TextView) adapterHeader.findViewById(R.id.text_movie_budget);
        textRevenue = (TextView) adapterHeader.findViewById(R.id.text_movie_revenue);
        textDuration = (TextView) adapterHeader.findViewById(R.id.text_movie_duration);
        textHomepage = (TextView) adapterHeader.findViewById(R.id.text_movie_homepage);
        textStatus = (TextView) adapterHeader.findViewById(R.id.text_movie_status);
        textDirectedBy = (TextView) adapterHeader.findViewById(R.id.text_movie_directed_by);
        textGenres = (TextView) adapterHeader.findViewById(R.id.text_movie_genres);
        textOverview = (TextView) adapterHeader.findViewById(R.id.text_movie_overview);
        textProductionCompanies = (TextView) adapterHeader.findViewById(R.id.text_production_companies);

        if (lowResBackdrop != null) {
            setBackdropImage(lowResBackdrop);
        }
        if (vibrantRgbColor != 0) {
            setOverlayAndToolbarColor(vibrantRgbColor);
        }

        setTitle(null);

        sendApiRequest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent;
                if (source == SOURCE_SEARCH_ACTIVITY) {
                    intent = new Intent(this, SearchActivity.class);
                } else if (source == SOURCE_WATCHLIST_ACTIVITY) {
                    intent = new Intent(this, WatchlistActivity.class);
                } else if (source == SOURCE_FAVORITES_ACTIVITY) {
                    intent = new Intent(this, FavouritesActivity.class);
                } else if (source == SOURCE_MOVIES_LISTS) {
                    intent = new Intent(this, MoviesListActivity.class);
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (intent.hasExtra("source")) {
            source = intent.getExtras().getInt("source");
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
            setTextTitle(sb.toString());
            if (movie.getTagline() != null && !movie.getTagline().isEmpty()) {
                textTagline.setText(movie.getTagline());
            }
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
            if (movie.getStatus() != null && !movie.getStatus().isEmpty()) {
                textStatus.setText(movie.getStatus());
            }
            if (movie.getCredits() != null && !movie.getCredits().getCrew().isEmpty()) {
                sb = new StringBuilder();
                boolean foundFirst = false;
                for (Crew crew : movie.getCredits().getCrew()) {
                    if (crew.getJob().equals("Director")) {
                        if (foundFirst) {
                            sb.append(", ");
                        }
                        sb.append(crew.getName());
                        foundFirst = true;
                    }
                }
                if (!sb.toString().equals("")) {
                    textDirectedBy.setText(sb.toString());
                }
            }
            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                sb = new StringBuilder();
                for (int i = 0; i < movie.getGenres().size(); i++) {
                    sb.append(movie.getGenres().get(i).getName());
                    if (i != (movie.getGenres().size() - 1))
                        sb.append(", ");
                }
                textGenres.setText(sb.toString());
            }
            textOverview.setText(movie.getOverview());
            if (movie.getProductionCompanies() != null && !movie.getProductionCompanies().isEmpty()) {
                sb = new StringBuilder();
                for (int i = 0; i < movie.getProductionCompanies().size(); i++) {
                    sb.append(movie.getProductionCompanies().get(i).getName());
                    if (i != (movie.getProductionCompanies().size() - 1)) {
                        sb.append(", ");
                    }
                }
                textProductionCompanies.setText(sb.toString());
            }
            if (movie.getCredits() != null && !movie.getCredits().getCast().isEmpty()) {
                movieDetailsAdapter.setCastList(movie.getCredits().getCast());
            }
            if (movie.getTrailers() != null && !movie.getTrailers().getYoutube().isEmpty()) {
                List<YoutubeTrailer> trailers = new ArrayList<>();
                for (YoutubeTrailer trailer : movie.getTrailers().getYoutube()) {
                    if (trailer.getType().equals("Trailer")) {
                        trailers.add(trailer);
                    }
                }
                movieDetailsAdapter.setTrailers(trailers);
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
                    setBackdropImage(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private String getRequestUrl() {
        return UrlEndpoints.URL_API_MOVIE + movieId.toString() + UrlEndpoints.URL_PARAM_API_KEY + UrlEndpoints.URL_PARAM_APPEND;
    }

    private String getBackdropRequestUrl() {
        return UrlEndpoints.URL_API_IMAGES + UrlEndpoints.URL_BACKDROP_LARGE + movie.getBackdropPath();
    }

    @Override
    protected void addToFavourites() {
        Toast.makeText(MovieDetailsActivity.this, Repository.getInstance().toggleFavorite(movieId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void addToWatchlist() {
        Toast.makeText(MovieDetailsActivity.this, Repository.getInstance().toggleWatchlist(movieId), Toast.LENGTH_SHORT).show();
    }
}
