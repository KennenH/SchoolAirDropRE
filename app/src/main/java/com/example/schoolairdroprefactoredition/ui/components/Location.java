package com.example.schoolairdroprefactoredition.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.amap.api.location.AMapLocation;
import com.example.schoolairdroprefactoredition.R;

public class Location extends ConstraintLayout {

    private ImageView mIcon;
    private TextView mArea;

    public Location(Context context) {
        this(context, null);
    }

    public Location(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Location(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.component_location_light, this, true);

        mIcon = findViewById(R.id.location_icon);
        mArea = findViewById(R.id.location_area);
    }

    public void setLocationName(String name) {
        mArea.setText(name);
    }
}
