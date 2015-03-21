package com.learning.android.movieman.backend;

import android.content.Context;
import android.os.AsyncTask;

import com.learning.android.movieman.model.MovieSmall;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by adrianrusu on 3/21/15.
 */
public class Repository {

    private static final String API_KEY_PARAM = "?api_key=b59556d48c2a757635e500ba15896257";
    private static final String API_ADDRESS = "http://api.themoviedb.org/3/";
    private static Repository instance;
    private DatabaseHandler dbHandler;

    private Repository(Context context) {
        dbHandler = new DatabaseHandler(context);
    }

    public static Repository getInstance() {
        return instance;
    }

    public static void setInstance(Repository instance) {
        Repository.instance = instance;
    }

    public List<MovieSmall> getPopularMovies() {
        List<MovieSmall> result = new ArrayList<>();

        try {
            JSONObject popularMoviesJson = new RecievePopularMovies().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public interface RepositoryCallback {
        void onDataLoaded(Repository repository);
    }

    private static class DataLoaderTask extends AsyncTask<RepositoryCallback, Void, Repository> {
        private RepositoryCallback[] callbacks;
        private Context context;


        private DataLoaderTask(Context context) {
            this.context = context;
        }

        @Override
        protected Repository doInBackground(RepositoryCallback... params) {
            this.callbacks = params;
            return new Repository(context);
        }

        @Override
        protected void onPostExecute(Repository repository) {
            for (RepositoryCallback callback : callbacks) {
                callback.onDataLoaded(repository);
            }
        }
    }

    private class RecievePopularMovies extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder(API_ADDRESS);
            sb.append("movie/now_playing");
            sb.append(API_KEY_PARAM);

            JSONObject result = null;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(sb.toString());
                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    StringBuilder stringBuilder = new StringBuilder();
                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = new JSONObject(stringBuilder.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}
