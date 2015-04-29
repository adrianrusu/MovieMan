package com.learning.android.movieman.activity;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.learning.android.movieman.R;
import com.learning.android.movieman.adapter.MovieDetailsRecyclerAdapter;
import com.learning.android.movieman.fragment.NavigationDrawerFragment;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MovieDetailsBaseActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    protected MovieDetailsRecyclerAdapter movieDetailsAdapter;
    private Toolbar toolbar;
    private View imageBackdrop;
    private View overlayView;
    private View recyclerViewBackground;
    private TextView textTitle;
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

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationDrawerFragment navDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), null, R.id.fragment_navigation_drawer);

        flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        actionBarSize = getActionBarSize();
        toolbarColor = getResources().getColor(R.color.primaryColor);

        ObservableRecyclerView recyclerView = (ObservableRecyclerView) findViewById(R.id.recycler);
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(false);

        LinearLayout headerLinearLayout = new LinearLayout(this);
        headerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        headerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View detailsView = LayoutInflater.from(this).inflate(R.layout.recycler_details_header, null, false);
        View headerView = new View(this);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, flexibleSpaceImageHeight));
        headerView.setMinimumHeight(flexibleSpaceImageHeight);
        headerLinearLayout.addView(headerView);
        headerLinearLayout.addView(detailsView);

        movieDetailsAdapter = new MovieDetailsRecyclerAdapter(this, headerLinearLayout);
        recyclerView.setAdapter(movieDetailsAdapter);

        toolbar.setBackgroundColor(Color.TRANSPARENT);

        imageBackdrop = findViewById(R.id.image);
        overlayView = findViewById(R.id.overlay);

        textTitle = (TextView) findViewById(R.id.title);
        textTitle.setText(getTitle());
        setTitle(null);

        recyclerViewBackground = findViewById(R.id.list_background);
        final View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                recyclerViewBackground.getLayoutParams().height = contentView.getHeight();
            }
        });

        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
        recyclerViewBackground.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(recyclerViewBackground, flexibleSpaceImageHeight);
            }
        });
        ViewHelper.setTranslationY(overlayView, flexibleSpaceImageHeight);
        textTitle.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(textTitle, (int) (flexibleSpaceImageHeight - textTitle.getHeight() * scale));
                ViewHelper.setPivotX(textTitle, 0);
                ViewHelper.setPivotY(textTitle, 0);
                ViewHelper.setScaleX(textTitle, scale);
                ViewHelper.setScaleY(textTitle, scale);
            }
        });

        fab = findViewById(R.id.floating_actions_menu);
        ((FloatingActionsMenu) fab).setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                ((ObservableRecyclerView) findViewById(R.id.recycler)).smoothScrollBy(0, -flexibleSpaceImageHeight);
            }

            @Override
            public void onMenuCollapsed() {

            }
        });
        findViewById(R.id.floating_action_fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavourites();
            }
        });
        findViewById(R.id.floating_action_watch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWatchlist();
            }
        });
        fabMargin = getResources().getDimensionPixelSize(R.dimen.material_default_spacing);
        ViewHelper.setScaleX(fab, 0);
        ViewHelper.setScaleY(fab, 0);

        ScrollUtils.addOnGlobalLayoutListener(findViewById(R.id.recycler), new Runnable() {
            @Override
            public void run() {
                onScrollChanged(0, false, false);
            }
        });
    }

    protected void addToWatchlist() {
    }

    protected void addToFavourites() {
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1) + getResources().getDimensionPixelSize(R.dimen.app_bar_top_padding);
        a.recycle();
        return actionBarSize;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = flexibleSpaceImageHeight - actionBarSize;
        int minOverlayTransitionY = actionBarSize - overlayView.getHeight();
        ViewHelper.setTranslationY(overlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(imageBackdrop, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(recyclerViewBackground, Math.max(0, -scrollY + flexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(overlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(textTitle, 0);
        ViewHelper.setScaleX(textTitle, scale);
        ViewHelper.setScaleY(textTitle, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (flexibleSpaceImageHeight - textTitle.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        titleTranslationY = Math.max(getResources().getDimensionPixelSize(R.dimen.app_bar_top_padding), titleTranslationY);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(textTitle, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(textTitle, 0);
        }
    }

    protected void setBackdropImage(Bitmap image) {
        ((ImageView) imageBackdrop).setImageBitmap(image);
    }

    protected void setOverlayAndToolbarColor(int color) {
        overlayView.setBackgroundColor(color);
        toolbarColor = color;
    }

    protected void setTextTitle(String title) {
        textTitle.setText(title);
    }

}
