package com.example.dali_bike;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class CoustomTextOutLineView extends TextView {
    private boolean stroke = false;
    private float strokeWidth = 0.0f;
    private int strokeColor = Color.BLACK;

    public CoustomTextOutLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CoustomTextOutLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoustomTextOutLineView);
        stroke = a.getBoolean(R.styleable.CoustomTextOutLineView_textStroke, false);
        strokeWidth = a.getFloat(R.styleable.CoustomTextOutLineView_textStrokeWidth, 0.0f);
        strokeColor = a.getColor(R.styleable.CoustomTextOutLineView_textStrokeColor, Color.BLACK);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (stroke) {
            ColorStateList states = getTextColors();
            getPaint().setStyle(Paint.Style.STROKE);
            getPaint().setStrokeWidth(strokeWidth);
            setTextColor(strokeColor);
            super.onDraw(canvas);
            getPaint().setStyle(Paint.Style.FILL);
            setTextColor(states);

        }
        super.onDraw(canvas);
    }
}
