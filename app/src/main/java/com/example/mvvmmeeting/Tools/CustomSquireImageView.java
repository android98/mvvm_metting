package com.example.mvvmmeeting.Tools;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CustomSquireImageView extends AppCompatImageView {


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);

    }

    public CustomSquireImageView(Context context) {
        super(context);

    }

    public CustomSquireImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomSquireImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
