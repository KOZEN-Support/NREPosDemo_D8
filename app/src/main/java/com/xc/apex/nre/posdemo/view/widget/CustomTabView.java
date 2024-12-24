package com.xc.apex.nre.posdemo.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xc.apex.nre.posdemo.R;

import java.util.ArrayList;
import java.util.List;

public class CustomTabView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    private String tabText;
    private int selectedImageResId;
    private int unselectedImageResId;
    private int selectedTextColor;
    private int unselectedTextColor;
    private int selectedBackground;
    private int unselectedBackground;

    private boolean isSelected;

    private static List<CustomTabView> tabViews = new ArrayList<>();

    public CustomTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_tab_view, this, true);
        imageView = findViewById(R.id.tabImageView);
        textView = findViewById(R.id.tabTextView);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTabView, 0, 0);
        try {
            tabText = a.getString(R.styleable.CustomTabView_tabText);
            selectedImageResId = a.getResourceId(R.styleable.CustomTabView_selectedImage, 0);
            unselectedImageResId = a.getResourceId(R.styleable.CustomTabView_unselectedImage, 0);
            selectedTextColor = a.getColor(R.styleable.CustomTabView_selectedTextColor, Color.WHITE);
            unselectedTextColor = a.getColor(R.styleable.CustomTabView_unselectedTextColor, getResources().getColor(R.color.dark_98));
            selectedBackground = a.getResourceId(R.styleable.CustomTabView_selectedBackgroundTab, R.drawable.bg_tag_selected);
            unselectedBackground = a.getResourceId(R.styleable.CustomTabView_unselectedBackgroundTab, R.drawable.bg_tab_unselected);
            textView.setText(tabText);
            setSelected(false);
        } finally {
            a.recycle();
        }

        tabViews.add(this);
    }

    public void setTabSelected() {
        for (CustomTabView tabView : tabViews) {
            tabView.setSelected(false);
        }
        setSelected(true);
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        imageView.setImageResource(selected ? selectedImageResId : unselectedImageResId);
        textView.setTextColor(selected ? selectedTextColor : unselectedTextColor);
        setBackground(selected ? getResources().getDrawable(selectedBackground) : getResources().getDrawable(unselectedBackground));
    }

    public boolean isTabSelected() {
        return isSelected;
    }
}
