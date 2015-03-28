package com.learning.android.movieman.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.Movie;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

public class MovieDetailsActivity extends ActionBarActivity {

    private Long movieId;
    private Movie movie;

    private ImageView imageBackdrop;
    private LinearLayout titlePlaceHolder;
    private TextView textTitle;
    private TextView textOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieId = getIntent().getExtras().getLong("id");

        imageBackdrop = (ImageView) findViewById(R.id.image_movie_detail_header);
        titlePlaceHolder = (LinearLayout) findViewById(R.id.movie_title_placeholder);
        textTitle = (TextView) findViewById(R.id.text_movie_title);
        textOverview = (TextView) findViewById(R.id.text_movie_overview);

        sendApiRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        }
    }

    private void sendApiBackdropRequest() {
        System.out.println(getBackdropRequestUrl());

        ImageLoader imageLoader = Repository.getInstance().getImageLoader();
        imageLoader.get(getBackdropRequestUrl(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {
                    imageBackdrop.setImageBitmap(bitmap);
                    setTitlePlaceHolderColor(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void setTitlePlaceHolderColor(Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    titlePlaceHolder.setBackgroundColor(vibrant.getRgb());
                    textTitle.setTextColor(vibrant.getTitleTextColor());
                }
            }
        });
    }

    private String getRequestUrl() {
        return UrlEndpoints.URL_API_HOME + "movie" + "/" + movieId.toString() + UrlEndpoints.URL_PARAM_API_KEY;
    }

    private String getBackdropRequestUrl() {
        return UrlEndpoints.URL_API_IMAGES + "w1280" + movie.getBackdropPath();
    }
}
