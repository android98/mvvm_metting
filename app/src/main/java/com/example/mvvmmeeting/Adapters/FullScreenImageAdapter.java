package com.example.mvvmmeeting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.mvvmmeeting.R;

import java.io.File;
import java.util.ArrayList;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> imagePaths;
    private LayoutInflater inflater;


    public FullScreenImageAdapter(Activity activity, ArrayList<String> imagePaths) {
        this.activity = activity;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

//
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageDisplay;
        Button btnClose;


        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.layout_fullscreen_image, container, false);
        imageDisplay = inflate.findViewById(R.id.imgDisplay);
        btnClose = inflate.findViewById(R.id.btnClose);


        File file = new File(imagePaths.get(position));
        Uri uri = Uri.fromFile(file);
        Glide.with(activity)
                .load(uri)
                .into(imageDisplay);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();

            }
        });

        ((ViewPager) container).addView(inflate);
        return inflate;
    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view == ((RelativeLayout) o);
    }
}
