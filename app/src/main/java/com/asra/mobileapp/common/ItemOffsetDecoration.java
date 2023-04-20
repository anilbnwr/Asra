package com.asra.mobileapp.common;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;
    private boolean isGrid;

    public ItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }



    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId, boolean isGrid) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
        this.isGrid = isGrid;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int halfOffset = isGrid ? mItemOffset/2  : mItemOffset;
        Timber.i("%s, %s", mItemOffset, halfOffset);
        outRect.set(mItemOffset, halfOffset, mItemOffset, halfOffset);
    }
}
