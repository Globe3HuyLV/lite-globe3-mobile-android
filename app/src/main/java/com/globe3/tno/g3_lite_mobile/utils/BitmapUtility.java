package com.globe3.tno.g3_lite_mobile.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.TypedValue;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Utilities.ConvertSimpleImage;

import java.io.ByteArrayOutputStream;

public class BitmapUtility {
    public static Bitmap decodeBitmap(Resources res, Bitmap bitmap, int dpWidth, int dpHeight) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] bitmapBytes = stream.toByteArray();

        float px_width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, res.getDisplayMetrics());
        float px_height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpHeight, res.getDisplayMetrics());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        options.inSampleSize = calculateInSampleSize(options, (int) px_width, (int) px_height);

        options.inJustDecodeBounds = false;

        Bitmap bitmap_raw = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        int new_size = bitmap_raw.getWidth() < bitmap_raw.getHeight() ? bitmap_raw.getWidth() : bitmap_raw.getHeight();

        Matrix matrix = new Matrix();

        if (bitmap_raw.getWidth() > bitmap_raw.getHeight()) {
            matrix.postRotate(90);
        }

        return Bitmap.createBitmap(bitmap_raw, 0, 0, new_size, new_size, matrix, true);
    }

    public static Bitmap decodeBitmap(Resources res, byte[] bitmapBytes, int dpWidth, int dpHeight) {
        float px_width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, res.getDisplayMetrics());
        float px_height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpHeight, res.getDisplayMetrics());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        options.inSampleSize = calculateInSampleSize(options, (int) px_width, (int) px_height);

        options.inJustDecodeBounds = false;

        Bitmap bitmap_raw = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        int new_size = bitmap_raw.getWidth() < bitmap_raw.getHeight() ? bitmap_raw.getWidth() : bitmap_raw.getHeight();

        Matrix matrix = new Matrix();

        if (bitmap_raw.getWidth() > bitmap_raw.getHeight()) {
            matrix.postRotate(90);
        }

        return Bitmap.createBitmap(bitmap_raw, 0, 0, new_size, new_size, matrix, true);
    }

    public static Bitmap decodeBitmap(Resources res, int resId, int dpWidth, int dpHeight) {
        float px_width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, res.getDisplayMetrics());
        float px_height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpHeight, res.getDisplayMetrics());

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, (int) px_width, (int) px_height);

        options.inJustDecodeBounds = false;

        Bitmap bitmap_raw = BitmapFactory.decodeResource(res, resId, options);

        int new_size = bitmap_raw.getWidth() < bitmap_raw.getHeight() ? bitmap_raw.getWidth() : bitmap_raw.getHeight();

        Matrix matrix = new Matrix();

        if (bitmap_raw.getWidth() > bitmap_raw.getHeight()) {
            matrix.postRotate(90);
        }

        return Bitmap.createBitmap(bitmap_raw, 0, 0, new_size, new_size, matrix, true);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static byte[] encodeBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap flipBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static Bitmap decodeByteToBitMapRotate(byte[] arr) {
        Bitmap bitmap = ConvertSimpleImage.bytesToBitmap(arr, MagicalCamera.JPEG);
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }
}
