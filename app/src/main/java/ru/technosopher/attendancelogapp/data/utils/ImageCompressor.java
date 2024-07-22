package ru.technosopher.attendancelogapp.data.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ImageCompressor {

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    public static byte[] convertBitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}