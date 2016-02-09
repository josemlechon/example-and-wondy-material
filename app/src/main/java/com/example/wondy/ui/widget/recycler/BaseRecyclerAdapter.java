package com.example.wondy.ui.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jose on 08/02/15.
 *
 * @version 0.1.0
 * @since 1
 */
public abstract class BaseRecyclerAdapter<T extends BaseRecyclerAdapter.BaseViewHolder, V> extends RecyclerView.Adapter<T> {

    protected List<V> mDataSet;

    private int lastPosition = -1;

    public BaseRecyclerAdapter() {
        mDataSet = new ArrayList<>();
    }


    public View inflate(ViewGroup parent, @LayoutRes int idLayout) {
        return LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
    }


    @Override
    public void onBindViewHolder(T holder, int position) {

        V item = getItem(position);

        holder.populate(item, position);

        setAnimation(holder.itemView, position);
    }


    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public V getItem(int position) {
        if (position < mDataSet.size())
            return mDataSet.get(position);
        else
            return null;
    }


    public void setData(@NonNull ArrayList<V> data) {
        if (data == null) {
            mDataSet = new ArrayList<>();
        } else {
            mDataSet = data;
        }
        notifyDataSetChanged();
    }


    public void addData(@NonNull ArrayList<V> data) {
        if (data == null) return;

        if (mDataSet == null) mDataSet = new ArrayList<>();


        int sizeCurrentRange = mDataSet.size();

        mDataSet.addAll(data);

        notifyItemRangeInserted(sizeCurrentRange, data.size());
    }

    public void replace(int i, V item) {
        mDataSet.set(i, item);
        notifyItemChanged(i);
    }


    public void addItem(int position, V model) {

        mDataSet.add(position, model);
        notifyItemInserted(position);

    }

    public void moveItem(int fromPosition, int toPosition) {

        final V model = mDataSet.remove(fromPosition);
        mDataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);

    }



    public V removeItem(int position) {

        V data =  mDataSet.remove(position);
        notifyItemRemoved(position);

        return data;
    }


    public void clear() {

        mDataSet.clear();
        notifyDataSetChanged();
    }

    public static abstract class BaseViewHolder<VAL> extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void populate(VAL item, int position);
    }
}
