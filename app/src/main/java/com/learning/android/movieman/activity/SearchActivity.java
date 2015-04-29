package com.learning.android.movieman.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.learning.android.movieman.R;
import com.learning.android.movieman.fragment.NavigationDrawerFragment;
import com.learning.android.movieman.fragment.SearchFragment;

public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationDrawerFragment navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), null, R.id.fragment_navigation_drawer);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            setTitle(query);
            SearchFragment searchFragment = SearchFragment.newInstance(query);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_search, searchFragment).commit();
        }
    }
}
