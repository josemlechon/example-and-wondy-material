package com.example.wondy.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wondy.R;
import com.example.wondy.app.AppConfig;
import com.example.wondy.model.Center;
import com.example.wondy.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author jose m lechon on 08/02/2016
 * @version 0.1.0
 * @since 1
 */
public class DetailActivity extends BaseActivity {

    private static final String EXTRA_CENTER = DetailActivity.class.getCanonicalName() + "#EXTRA_CENTER";

    public static Intent intentActivity(@NonNull Center center, @NonNull Context context) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_CENTER, center);
        return intent;
    }

    private Center mCenter;

    //Views
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.textview_description)
    TextView mTextViewDescription;
    @Bind(R.id.textview_address)
    TextView mTextViewAddress;
    @Bind(R.id.textview_services)
    TextView mTextViewServices;

    @Bind(R.id.recyclerview_images)
    RecyclerView mRecyclerViewImages;

    @Bind(R.id.imageview_map)
    ImageView mImageViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getComponent().inject(this);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(Boolean.TRUE);

        mCenter = getIntent().getParcelableExtra(EXTRA_CENTER);

        initViews();
    }


    private void initViews(){
        if(mCenter == null) finish(); //unlikely

        setTitle(mCenter.name);

        mTextViewDescription.setText(mCenter.description);
        mTextViewAddress.setText(mCenter.address);

        mTextViewServices.setText(getServicesFormatted(mCenter.services));

        if(mCenter.images != null && mCenter.images.size()>0) {

            mRecyclerViewImages.setHasFixedSize(Boolean.TRUE);
            mRecyclerViewImages.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

            GalleryImagesAdapter adapter = new GalleryImagesAdapter();
            adapter.setData(new ArrayList<>(mCenter.images));

            mRecyclerViewImages.setAdapter(adapter);

        }else{

            mRecyclerViewImages.setVisibility( View.GONE );
        }


        String  coordinates = mCenter.latitude +","+ mCenter.longitude;
        Glide.with(this)
                .load(AppConfig.getStaticMap(coordinates))
                .placeholder(R.drawable.map_pinhole)
                .error(R.drawable.map_pinhole)
                .centerCrop()
                .into(mImageViewMap);
    }


    private String getServicesFormatted(List<String> services){

        if(services == null || services.isEmpty() ) return "";

        String formatted = getString(R.string.services_formatted);
        for(String item : services){
            formatted += "\n- " +item;
        }

        return formatted;
    }
}
