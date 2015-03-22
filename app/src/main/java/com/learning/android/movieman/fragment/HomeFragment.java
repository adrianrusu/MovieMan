package com.learning.android.movieman.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.learning.android.movieman.R;
import com.learning.android.movieman.backend.Repository;
import com.learning.android.movieman.model.MovieSmall;
import com.learning.android.movieman.util.JsonKeys;
import com.learning.android.movieman.util.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_ID;
import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_POPULARITY;
import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_POSTER_PATH;
import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_RELEASE_DATE;
import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_TITLE;
import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_VOTE_AVERAGE;
import static com.learning.android.movieman.util.JsonKeys.Movie.KEY_VOTE_COUNT;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView moviesRecyclerView;

    private RequestQueue requestQueue;
    private List<MovieSmall> movies = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        requestQueue = Repository.getInstance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(), "", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJsonResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                Log.d(this.getClass().getCanonicalName(), error.toString());
            }
        });
        requestQueue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        moviesRecyclerView = (RecyclerView) view.findViewById(R.id.home_movies_list);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inflate the layout for this fragment
        return view;
    }

    private String getRequestUrl() {
        return new StringBuilder(UrlEndpoints.NOW_PLAYING_URL).append(UrlEndpoints.URL_PARAM_API_KEY).toString();
    }

    private void parseJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 1) {
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            JSONArray arrayMovies = jsonObject.getJSONArray(JsonKeys.KEY_RESULTS);
            for (int i = 0; i < arrayMovies.length(); i++) {
                JSONObject jsonMovie = arrayMovies.getJSONObject(i);
                MovieSmall movie = new MovieSmall(jsonMovie.getLong(KEY_ID),
                        jsonMovie.getString(KEY_TITLE),
                        sdf.parse(jsonMovie.getString(KEY_RELEASE_DATE)),
                        jsonMovie.getDouble(KEY_VOTE_AVERAGE),
                        new Integer(jsonMovie.getInt(KEY_VOTE_COUNT)),
                        jsonMovie.getString(KEY_POSTER_PATH),
                        jsonMovie.getDouble(KEY_POPULARITY)
                );
                movies.add(movie);
            }
            Toast.makeText(getActivity(), "Processed " + movies.size() + " movies from JSON", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}