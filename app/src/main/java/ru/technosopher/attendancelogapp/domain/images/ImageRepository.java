package ru.technosopher.attendancelogapp.domain.images;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import io.reactivex.rxjava3.core.Single;
import ru.technosopher.attendancelogapp.domain.entities.ImageEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface ImageRepository {

    void getProfileImage(@NonNull String imageUrl, Consumer<Status<ImageEntity>> callback);

    Single<String> uploadProfileImage(@NonNull String id, @NonNull Bitmap imageBitmap);
}