package com.example.diplommobileapp.data.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.yandex.mapkit.mapview.MapView;

public class CustomMapView extends MapView {
    private ViewParent mViewParent;

    public CustomMapView(Context context) {
        super(context);
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setViewParent(@Nullable final ViewParent viewParent) {
        mViewParent = viewParent;
    }

}
