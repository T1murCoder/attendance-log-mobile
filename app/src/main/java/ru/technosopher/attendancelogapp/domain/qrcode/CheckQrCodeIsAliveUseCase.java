package ru.technosopher.attendancelogapp.domain.qrcode;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class CheckQrCodeIsAliveUseCase {

    private final QrCodeRepository repository;

    public CheckQrCodeIsAliveUseCase(QrCodeRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String lessonId, @NonNull Consumer<Status<QrCodeEntity>> callback){
        repository.checkQrCodeIsAlive(lessonId, callback);
    }
}
