package ru.technosopher.attendancelogapp.domain.entities;

import android.net.Uri;

public class ImageEntity {

    private final Uri imageUri;

    public ImageEntity(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}