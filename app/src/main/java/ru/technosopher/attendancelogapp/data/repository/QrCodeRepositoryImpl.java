package ru.technosopher.attendancelogapp.data.repository;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.QrCodeDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.QrCodeApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.data.utils.Mapper;
import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.qrcode.QrCodeRepository;

public class QrCodeRepositoryImpl implements QrCodeRepository {

    private static QrCodeRepositoryImpl INSTANCE;

    private final QrCodeApi qrCodeApi = RetrofitFactory.getInstance().getQrCodeApi();

    public static QrCodeRepositoryImpl getInstance() {
        if (INSTANCE == null) INSTANCE = new QrCodeRepositoryImpl();
        return INSTANCE;
    }
    @Override
    public void checkQrCodeIsAlive(@NonNull String lessonId, @NonNull Consumer<Status<QrCodeEntity>> callback) {
        qrCodeApi.checkQrCodeIsAlive(new QrCodeDto(null, lessonId, null, null)).enqueue(new CallToConsumer<>(
                callback,
                Mapper::fromQrDtoToEntity
        ));
    }
}
