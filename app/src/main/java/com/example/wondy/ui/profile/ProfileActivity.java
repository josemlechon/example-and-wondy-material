package com.example.wondy.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wondy.R;
import com.example.wondy.app.AppConfig;
import com.example.wondy.ui.BaseActivity;
import com.example.wondy.ui.widget.images.CircleTransform;
import com.example.wondy.util.L;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author jose m lechon on 09/02/2016
 * @version 0.1.0
 * @since 1
 */
public class ProfileActivity extends BaseActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    public static final int REQUEST_TAKE_PHOTO = 15107;

    private String mCurrentPhotoPath;

    @Inject
    AppConfig mAppConfig;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.imageview_profile)
    ImageView mImageViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        getComponent().inject(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews() {

        Glide.with(this)
                .load(mAppConfig.getUserImagePath())
                .transform(new CircleTransform(this))
                .error(android.R.drawable.sym_def_app_icon)
                .into(mImageViewProfile);
    }

    @OnClick({R.id.button_picture, R.id.imageview_profile})
    void onPictureButtonClicked(View view) {

        dispatchTakePictureIntent();
    }


    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File myFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), imageFileName + ".jpg");

        mCurrentPhotoPath = String.format("file:%s", myFile.getAbsolutePath());

        return myFile;
    }


    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;

            try {

                photoFile = createImageFile();

            } catch (IOException ex) {

                L.LOGE(TAG, "error ", ex);
                Snackbar.make(mToolbar, getString(R.string.error_getting_data), Snackbar.LENGTH_LONG).show();
            }

            if (photoFile != null) {

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            try {

                mAppConfig.setUserImagePath(mCurrentPhotoPath);

                Glide.with(this)
                        .load(mCurrentPhotoPath)
                        .transform(new CircleTransform(this))
                        .into(mImageViewProfile);

            } catch (Exception ex) {
                L.LOGE(TAG, "Error getting image", ex);

                Snackbar.make(mToolbar, getString(R.string.error_getting_data), Snackbar.LENGTH_LONG).show();
            }


        }
    }
}

