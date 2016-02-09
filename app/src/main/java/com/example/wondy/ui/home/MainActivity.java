package com.example.wondy.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wondy.R;
import com.example.wondy.app.AppConfig;
import com.example.wondy.controller.FeedController;
import com.example.wondy.events.CentersEvent;
import com.example.wondy.model.Center;
import com.example.wondy.ui.BaseActivity;
import com.example.wondy.ui.detail.DetailActivity;
import com.example.wondy.ui.home.ListCentersFragment.OnListPlacesInteractionListener;
import com.example.wondy.ui.profile.ProfileActivity;
import com.example.wondy.ui.widget.images.CircleTransform;
import com.example.wondy.util.helper.ViewHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author jose m lechon
 * @version 0.1.0
 * @since 1
 */
public class MainActivity extends BaseActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnInfoWindowClickListener,
        OnListPlacesInteractionListener {


    private GoogleMap mMap;
    private HashMap<String, Center> mMapMarkers = new HashMap<>();

    @Inject
    FeedController mFeedController;

    @Inject
    EventBus mEventBus;

    @Inject
    AppConfig mAppConfig;

    //Views
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    ImageView mImageViewNavigationUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponent().inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        initMap();

        initViews();
    }


    private void initMap() {

        ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map)).getMapAsync(this);
    }

    private void initViews() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        View header = LayoutInflater.from(this).inflate(R.layout.layout_nav_header_main, null);
        mImageViewNavigationUser = ButterKnife.findById(header, R.id.imageview_navigation_user);

        mImageViewNavigationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHelper.blockViewShortTime(view);

                openProfile();
            }
        });

        mNavigationView.addHeaderView(header);


        mNavigationView.setNavigationItemSelectedListener(this);


        Glide.with(this)
                .load(mAppConfig.getUserImagePath())
                .transform(new CircleTransform(this))
                .error(android.R.drawable.sym_def_app_icon)
                .into(mImageViewNavigationUser);

    }


    @Override
    protected void onStart() {
        super.onStart();

        mEventBus.register(this);
        mFeedController.requestFeed();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) refresh();

        else return super.onOptionsItemSelected(item);

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            openProfile();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Subscribe(priority = 1)
    @SuppressWarnings("unused")
    public void onEvent(CentersEvent event) {

        if (!event.isSuccess()) {

            Snackbar.make(mToolbar, getResources().getString(R.string.error_getting_data), Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        addMapPoints(event.getData());
    }


    private void addMapPoints(List<Center> centers) {

        if (centers == null) return;

        mMap.clear();
        mMapMarkers.clear();

        boolean placeCamera = Boolean.TRUE;

        for (Center item : centers) {

            MarkerOptions markerOptions = item.getMapMarker();

            if (markerOptions != null) {

                Marker marker = mMap.addMarker(markerOptions);
                mMapMarkers.put(marker.getId(), item);

                if (placeCamera) {

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.latitude, item.longitude), 10));
                    placeCamera = Boolean.FALSE;
                }
            }
        }
    }


    private void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {


        if (!mMapMarkers.containsKey(marker.getId())) return;

        Center center = mMapMarkers.get(marker.getId());

        if (center == null) {
            Toast.makeText(getApplicationContext(), R.string.error_center_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        openDetailCenter(center);
    }

    private void openDetailCenter(@NonNull Center center) {

        Intent intent = DetailActivity.intentActivity(center, getApplicationContext());
        startActivity(intent);
    }

    @Override
    public void onPlaceSelected(Center center) {
        openDetailCenter(center);
    }

    private void refresh() {

        if (!ViewHelper.checkNetworkValidAndShowAlert(mToolbar)) return;

        mFeedController.requestCleanFeed();
    }



    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
