package com.asra.mobileapp.common.imageloader;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.asra.mobileapp.common.UiUtils;
import com.asra.mobileapp.ui.base.ETFragment;

import java.io.File;

import androidx.annotation.Nullable;
import timber.log.Timber;

public class GlideHelper {

    public static void setImage(ETFragment fragment, ImageView imageView, String url, int errorImage) {
        setImage(fragment, imageView, url, errorImage, null);
    }



    public static void setImage(ETFragment fragment, ImageView imageView, String url, int errorImage, View progressContainer) {
        if (fragment.isDetached() || fragment.isRemoving() || fragment.getContext() == null) {
            Timber.w("Image loading aborted. Context may be NULL");
            return;
        }
        url = getETImage(url);
        if (progressContainer != null)
            progressContainer.setVisibility(View.VISIBLE);

        imageView.setVisibility(View.VISIBLE);
        Glide.with(fragment.getContext())
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        if (fragment.isDetached() || fragment.isRemoving()) {
                            return true;
                        }
                        Timber.w(e, "Error loading image");

                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (fragment.isDetached() || fragment.isRemoving()) {
                            return true;
                        }
                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .error(errorImage)
                .placeholder(errorImage)
                .into(imageView);
    }

    public static void setImage(ETFragment fragment, ImageView imageView, String url, View progressContainer) {
        if (fragment.isDetached() || fragment.isRemoving() || fragment.getContext() == null) {
            Timber.w("Image loading aborted. Context may be NULL");
            return;
        }
        url = getETImage(url);
        if (progressContainer != null)
            progressContainer.setVisibility(View.VISIBLE);

        Glide.with(fragment.getContext())
                .load(url)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        if (fragment.isDetached() || fragment.isRemoving()) {
                            return true;
                        }
                        Timber.w(e, "Error loading image");

                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (fragment.isDetached() || fragment.isRemoving()) {
                            return true;
                        }
                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }


    public static void setImage(ETFragment fragment, ImageView imageView, String url) {
        if (fragment.isDetached() || fragment.isRemoving() || fragment.getContext() == null) {
            Timber.w("Image loading aborted. Context may be NULL");
            return;
        }
        url = getETImage(url);
        Glide.with(fragment.getContext())
                .load(url)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        if (fragment.isDetached() || fragment.isRemoving()) {
                            return true;
                        }
                        Timber.w(e, "Error loading image");

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (fragment.isDetached() || fragment.isRemoving()) {
                            return true;
                        }
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void setImage(ImageView imageView, String url, View progressContainer) {
        if (imageView == null || imageView.getContext()== null) {
            Timber.w("Image loading aborted. Context may be NULL");
            return;
        }
        url = getETImage(url);
        if (progressContainer != null)
            progressContainer.setVisibility(View.VISIBLE);

        Glide.with(imageView.getContext())
                .load(url)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        if (imageView.getContext() == null) {
                            return true;
                        }
                        Timber.w(e, "Error loading image");

                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageView.getContext() == null) {
                            return true;
                        }
                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void setImage(ImageView imageView, String url, int errorImage, View progressContainer) {
        if (imageView == null || imageView.getContext() == null) {
            Timber.w("Image loading aborted. Context may be NULL");
            return;
        }
        url = getETImage(url);
        if (progressContainer != null)
            progressContainer.setVisibility(View.VISIBLE);

        Glide.with(imageView.getContext())
                .load(url)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        if (imageView.getContext() == null) {
                            return true;
                        }
                        Timber.w(e, "Error loading image");

                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageView.getContext() == null) {
                            return true;
                        }
                        if (progressContainer != null)
                            progressContainer.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .error(errorImage)
                .into(imageView);
    }

    private static String getETImage(String url){
        String etImageUrl =  UiUtils.getETAbsoluteURL(url);
        Timber.d("Image URL - %s", etImageUrl);
        return etImageUrl;
    }

    public static void setImage(ImageView imageView, File imageFile){

        Glide.with(imageView.getContext())
                .asBitmap().load(imageFile).into(imageView);
    }

    public static void setImageUri(ImageView imageView, Uri uri) {
        Glide.with(imageView.getContext())
                .asBitmap().load(uri).into(imageView);
    }
}
