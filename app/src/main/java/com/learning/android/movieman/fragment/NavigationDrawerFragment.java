package com.learning.android.movieman.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learning.android.movieman.R;
import com.learning.android.movieman.activity.AboutActivity;
import com.learning.android.movieman.activity.MainActivity;
import com.learning.android.movieman.adapter.NavbarRecyclerAdapter;
import com.learning.android.movieman.adapter.RecyclerViewSelectionListener;

public class NavigationDrawerFragment extends Fragment implements RecyclerViewSelectionListener {

    private static final String PREF_FILE_NAME = "testpref";
    private static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private String[] titles = {"Home", "Watchlist", "Favorites", "About"};
    private int[] icons = {R.drawable.ic_home, R.drawable.ic_theater, R.drawable.ic_star, R.drawable.ic_xml};

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private boolean userLearnedDrawer;
    private boolean fromSavedInstanceState;

    public NavigationDrawerFragment() {
    }

    private static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    private static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            fromSavedInstanceState = true;
        }
    }

    public void setUp(DrawerLayout layout, Toolbar toolbar, int fragmentId) {
        drawerLayout = layout;
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!userLearnedDrawer) {
                    userLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, Boolean.toString(userLearnedDrawer));
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        View containerView = getActivity().findViewById(fragmentId);

        if (!userLearnedDrawer && !fromSavedInstanceState) {
            drawerLayout.openDrawer(containerView);
        }

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.navdrawer_recycler);
        recyclerView.setHasFixedSize(true);

        NavbarRecyclerAdapter adapter = new NavbarRecyclerAdapter(titles, icons, layout.getContext());
        adapter.setSelectionListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext()));

        return layout;
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = null;
        if (position == 1 && !getActivity().getClass().equals(MainActivity.class)) {
            intent = new Intent(getActivity(), MainActivity.class);
        } else if (position == 4 && !getActivity().getClass().equals(AboutActivity.class)) {
            intent = new Intent(getActivity(), AboutActivity.class);
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            drawerLayout.closeDrawers();
            startActivity(intent);
        }
    }
}
