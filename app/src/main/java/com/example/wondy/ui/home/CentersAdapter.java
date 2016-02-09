package com.example.wondy.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wondy.R;
import com.example.wondy.model.Center;
import com.example.wondy.ui.widget.recycler.BaseRecyclerAdapter;
import com.example.wondy.util.helper.ViewHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jose m lechon on 29/01/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class CentersAdapter extends BaseRecyclerAdapter<CentersAdapter.PlaceViewHolder, Center> {

    private Callback mCallback;

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_center, parent, false);

        return new PlaceViewHolder(view);
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    class PlaceViewHolder extends BaseRecyclerAdapter.BaseViewHolder<Center> {

        @Bind(R.id.textview_title)
        TextView mTextViewTitle;

        public PlaceViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void populate(Center item, int position) {

            mTextViewTitle.setText(item.name);
        }

        @OnClick(R.id.layout_root_item)
        void onItemRootViewClicked(View view){

            ViewHelper.blockViewShortTime(view);

            if(mCallback == null ) return;

            mCallback.onCenterClicked(getItem(getAdapterPosition()));
        }
    }


    public interface Callback {

        void onCenterClicked(Center center);
    }
}
