package com.learning.android.movieman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.fragment.NavigationDrawerFragment;
import com.learning.android.movieman.model.Movie;
import com.learning.android.movieman.util.JsonUtils;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends ActionBarActivity {

    private BarChart barChart;

    private List<Long> movieIds;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationDrawerFragment navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

        barChart = (BarChart) findViewById(R.id.bar_chart);
        barChart.setDescription("");
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.animateY(2500);

        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        barChart.getAxisLeft().setValueFormatter(new CurrencyValueFormatter());
        barChart.getAxisRight().setEnabled(false);

        initializeMovieIdsList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initializeMovieIdsList();
    }

    public void initializeMovieIdsList() {
        movieIds = Repository.getInstance().getDbHandler().getFavoriteMovieIds();
        getMovieDataFromApi();
    }

    private void getMovieDataFromApi() {
        if (movieIds.isEmpty()) {
            return;
        }
        movies = new ArrayList<>();

        for (Long movieId : movieIds) {
            sendApiRequest(movieId);
        }
    }

    private void sendApiRequest(Long movieId) {
        RequestQueue requestQueue = Repository.getInstance().getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.URL_API_MOVIE + movieId.toString() + UrlEndpoints.URL_PARAM_API_KEY, (String) null, new Response.Listener<JSONObject>() {
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

    private void handleResponse(JSONObject response) {
        movies.add(JsonUtils.parseMovieJsonResponse(response));

        if (movies.size() == movieIds.size()) {
            addMoviesToChart();
        }
    }

    private void handleError(VolleyError error) {

    }

    private void addMoviesToChart() {
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> yVals = new ArrayList<>();

        for (int i = 0; i < movies.size(); i++) {
            xVals.add(getUppercase(movies.get(i).getTitle()));
            yVals.add(new BarEntry(movies.get(i).getRevenue().floatValue() / 1000000, i));
        }

        BarDataSet dataSet = new BarDataSet(yVals, "Favorites by revenue");
        dataSet.setBarSpacePercent(35f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        barChart.setData(data);
        barChart.invalidate();
    }

    private String getUppercase(String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                sb.append(string.charAt(i));
            }
        }
        return sb.toString();
    }

    private class CurrencyValueFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return "$" + (int) value + " Mil";
        }
    }
}
