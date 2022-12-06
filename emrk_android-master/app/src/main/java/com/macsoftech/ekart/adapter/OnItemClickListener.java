package com.macsoftech.ekart.adapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Ramesh on 20/05/17.
 */

public interface OnItemClickListener<T> {
    void onItemClicked(RecyclerView.ViewHolder holder, T item);
}
