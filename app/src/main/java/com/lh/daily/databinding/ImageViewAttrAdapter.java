package com.lh.daily.databinding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by home on 2017/2/8.
 */

public class ImageViewAttrAdapter {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
    }

}
