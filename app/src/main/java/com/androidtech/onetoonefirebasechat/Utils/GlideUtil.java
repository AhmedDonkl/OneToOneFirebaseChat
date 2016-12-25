package com.androidtech.onetoonefirebasechat.Utils;

/**
 * Created by Ahmed Donkl on 11/14/2016.
 */


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.androidtech.instar.R;
import com.androidtech.instar.Widgets.CropCircleTransformation;
import com.bumptech.glide.Glide;

public class GlideUtil {
    public static void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(context, R.color.divider));
        Glide.with(context)
                .load(url)
                .placeholder(cd)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadFullImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(context, R.color.cardview_dark_background));
        Glide.with(context)
                .load(url)
              //  .fitCenter()
                //.placeholder(cd)
               // .crossFade()
                //.fitCenter()
                .into(imageView);
    }

    public static void loadProfileIcon(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.profile_image)
                .dontAnimate()
                .fitCenter()
                .bitmapTransform( new CropCircleTransformation(context))
                .into(imageView);
    }

    public static void loadProfileIconFromUri(Uri uri, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.profile_image)
                .dontAnimate()
                .fitCenter()
                .bitmapTransform( new CropCircleTransformation(context))
                .into(imageView);
    }
}
