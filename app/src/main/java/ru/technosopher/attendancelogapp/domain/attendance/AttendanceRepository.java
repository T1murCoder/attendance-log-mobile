package ru.technosopher.attendancelogapp.domain.attendance;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface AttendanceRepository {

    void changeStudentAttAndPoints(@NonNull String attId,
                                   @NonNull String lessonId,
                                   @NonNull String studentId,
                                   @NonNull Boolean isVisited,
                                   @NonNull String points,
                                   @NonNull Consumer<Status<Void>> callback);

}
