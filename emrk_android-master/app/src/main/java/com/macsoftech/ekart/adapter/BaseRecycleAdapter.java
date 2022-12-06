package com.macsoftech.ekart.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramesh on 13/07/17.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mList = new ArrayList<>();
    private List<T> filterList = new ArrayList<>();
    protected final LayoutInflater inflater;
    protected Context mContext;
    protected OnItemClickListener onItemClickListener;

    public BaseRecycleAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addItems(List<T> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        mList = new ArrayList<>(items);
        filterList = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        mList.add(item);
        filterList = new ArrayList<>(mList);
        notifyItemInserted(mList.size() - 1);
    }

    public void addItemsAll(List<T> items) {
        int offset = mList.size();
        mList.addAll(items);
        filterList.addAll(items);
        notifyDataSetChanged();
//        notifyItemChanged(offset, mList.size());
    }

    public void clear() {
        mList.clear();
        filterList.clear();
        notifyDataSetChanged();
    }

    public List<T> getAllItems() {
        return filterList;
    }

    public void removeItem(int position) {
        T item = getItem(position);
        if (item != null) {
            mList.remove(position);
            filterList.remove(position);
//            notifyDataSetChanged();
            notifyItemRemoved(position);
        }
    }

    public List<T> getOriginalList() {
        return mList;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder.itemView != null && onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(holder, getItem(position));
                }
            });

        }
    }

//    public abstract void onBindViewHolder(OffersHolder holder, int position);

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public T getItem(int position) {
        return filterList.get(position);
    }

    // Do Search...
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(mList);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (T item : mList) {
                        if (hasFilter(item, text)) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

    protected boolean hasFilter(T item, String text) {
        return false;
    }
}

