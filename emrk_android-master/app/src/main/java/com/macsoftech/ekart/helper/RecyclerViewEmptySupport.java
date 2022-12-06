package com.macsoftech.ekart.helper;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewEmptySupport extends RecyclerView {
    private View emptyView;
    private View loadingView;
    boolean loading;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            RecyclerView.Adapter<?> adapter = getAdapter();
            if (adapter != null && emptyView != null) {
                if (loading && loadingView != null) {
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                    loadingView.setVisibility(VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setAlpha(0f);
                    emptyView.animate().alpha(1).start();
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                    if (loadingView != null) {
                        loadingView.setVisibility(GONE);
                    }
                } else {
                    emptyView.setVisibility(View.GONE);
                    //
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                    setAlpha(0f);
                    animate().alpha(1)
                            .setStartDelay(100)
                            .setDuration(500)
                            .start();
                    if (loadingView != null) {
                        loadingView.setVisibility(GONE);
                    }
                }
            }

        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        emptyObserver.onChanged();
    }
}