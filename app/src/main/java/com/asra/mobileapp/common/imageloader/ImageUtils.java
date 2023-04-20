package com.asra.mobileapp.common.imageloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;


import com.asra.mobileapp.ETApplication;
import com.asra.mobileapp.ui.base.ETFragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

public class ImageUtils {

    public static final String IMAGE_FOLDER = "EvolveGT";




    private static Bitmap getBitmapFromURI(Context context, Uri uri) {
        try {

            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input == null) {
                return null;
            }
            return BitmapFactory.decodeStream(input);
        } catch (FileNotFoundException e) {
            Timber.e(e,"File Open Failed");
        }
        return null;

    }

    public static String prepareImageFromUri(Uri cameraImage) {

        String profileImage = "et.jpg";
        File file = new File(ETApplication.getInstance().getFilesDir(), profileImage);
        if (file.exists()) {
            file.delete();
        }

        try {
            Bitmap photoBitmap = getBitmapFromURI(ETApplication.getInstance(), cameraImage);
            photoBitmap = resizeImage(photoBitmap);

            int imageRotation = getImageRotation(cameraImage);
            if (imageRotation != 0)
                photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

            FileOutputStream out = new FileOutputStream(file);

            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
            photoBitmap.recycle();

        } catch (Exception e) {
            Timber.e(e, "Image Resize failed.");
        }


        return file.getAbsolutePath();
    }

    public static String prepareImageFromBitmap(Bitmap photoBitmap) {

        String profileImage = "et.jpg";
        File file = new File(ETApplication.getInstance().getFilesDir(), profileImage);


        try {
            photoBitmap = resizeImage(photoBitmap);

            int imageRotation = getImageRotation(photoBitmap);
            if (imageRotation != 0)
                photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

            FileOutputStream out = new FileOutputStream(file);

            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            photoBitmap.recycle();

        } catch (Exception e) {
            Timber.e(e, "Image Resize failed.");
        }


        return file.getAbsolutePath();
    }

    private static int getImageRotation(final Uri imageFile) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(imageFile.getPath());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private static int getImageRotation(final Bitmap bitmap) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            exif = new ExifInterface(is);
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }



    private static int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }

    private static Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
        Matrix matrix = new Matrix();
        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }



    public static Bitmap resizeImage(Bitmap bitmap) {
        // Get the dimensions of the View


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int targetW = (int) (width * 0.75);
        int targetH = (int) (height * 0.75);

        float scaleWidth = ((float) targetW) / width;
        float scaleHeight = ((float) targetH) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    public static void openCamera(ETFragment fragment) {
//
    }

    public static boolean cameraCheck(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean isAvailable = intent.resolveActivity(context.getPackageManager()) != null;

        if (!isAvailable) {
            Context appContext = context.getApplicationContext();
            Toast.makeText(appContext,
                    "Camera not found", Toast.LENGTH_LONG).show();
        }
        return isAvailable;
    }
}
