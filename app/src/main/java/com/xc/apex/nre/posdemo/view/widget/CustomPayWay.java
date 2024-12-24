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

public class CustomPayWay extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    private String wayStr;
    private int imageResId;
    private int selectedBackground;
    private int unselectedBackground;
    private int selectedTextColor;
    private int unselectedTextColor;

    private boolean isSelected;

    private static List<CustomPayWay> payViews = new ArrayList<>();

    public CustomPayWay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_pay_way, this, true);
        imageView = findViewById(R.id.img);
        textView = findViewById(R.id.way);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomPayWay, 0, 0);
        try {
            wayStr = a.getString(R.styleable.CustomPayWay_payWayTxt);
            imageResId = a.getResourceId(R.styleable.CustomPayWay_payWayImage, 0);
            selectedBackground = a.getResourceId(R.styleable.CustomPayWay_selectedBackground, R.drawable.bg_pay_type_card_selected);
            unselectedBackground = a.getResourceId(R.styleable.CustomPayWay_unselectedBackground, R.drawable.bg_radius_33_white);
            selectedTextColor = a.getColor(R.styleable.CustomPayWay_selectedTextColorPay, Color.BLACK);
            unselectedTextColor = a.getColor(R.styleable.CustomPayWay_unselectedTextColorPay, getResources().getColor(R.color.dark_98));

            textView.setText(wayStr);
            textView.setTextColor(unselectedTextColor);
            imageView.setImageResource(imageResId);
            setBackground(getResources().getDrawable(R.drawable.bg_radius_33_white));
            setSelected(false);
        } finally {
            a.recycle();
        }

        payViews.add(this);
    }

    public void setPayWaySelected() {
        for (CustomPayWay way : payViews) {
            way.setSelected(false);
        }
        setSelected(true);
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            textView.setTextColor(selectedTextColor);
            setBackground(getResources().getDrawable(selectedBackground));
        } else {
            textView.setTextColor(unselectedTextColor);
            setBackground(getResources().getDrawable(unselectedBackground));
        }
    }

    public boolean isTabSelected() {
        return isSelected;
    }
}
