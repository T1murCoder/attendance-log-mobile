package ru.technosopher.attendancelogapp.domain.qrcode;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface QrCodeRepository {
    void checkQrCodeIsAlive(@NonNull String lessonId, @NonNull Consumer<Status<QrCodeEntity>> callback);
}
