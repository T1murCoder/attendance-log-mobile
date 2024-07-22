package ru.technosopher.attendancelogapp.data.repository;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.AttendanceDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.AttendanceApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.domain.attendance.AttendanceRepository;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class AttendanceRepositoryImpl implements AttendanceRepository {

    private static AttendanceRepositoryImpl INSTANCE;

    private final AttendanceApi attendanceApi = RetrofitFactory.getInstance().getAttendanceApi();

    public static AttendanceRepositoryImpl getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new AttendanceRepositoryImpl();
        return INSTANCE;
    }

    @Override
    public void changeStudentAttAndPoints(@NonNull String attId, @NonNull String lessonId, @NonNull String studentId, @NonNull Boolean isVisited, @NonNull String points, @NonNull Consumer<Status<Void>> callback) {
        attendanceApi.setAttendanceAndPointsToStudent(attId, new AttendanceDto(attId, studentId, lessonId, isVisited, points, null)).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }
}
