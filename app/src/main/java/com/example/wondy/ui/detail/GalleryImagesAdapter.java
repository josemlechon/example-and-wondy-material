package com.example.wondy.ui.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wondy.R;
import com.example.wondy.ui.widget.recycler.BaseRecyclerAdapter;

import butterknife.Bind;

/**
 * Created by jose on 08/09/15.
 *
 * @version 0.1.0
 * @since 1
 */
public class GalleryImagesAdapter extends BaseRecyclerAdapter<GalleryImagesAdapter.PhotoViewHolder, String> {


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflate(parent, R.layout.item_gallery_image);
        return new PhotoViewHolder(view);
    }


    public class PhotoViewHolder extends BaseRecyclerAdapter.BaseViewHolder<String> {

        @Bind(R.id.imageview_photo)
        ImageView mImageViewPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void populate(final String item, int position) {

            Context context = mImageViewPhoto.getContext();

            Glide.with(context)
                    .load(item)
                    .centerCrop()
                    .into(mImageViewPhoto);

        }


    }
}
