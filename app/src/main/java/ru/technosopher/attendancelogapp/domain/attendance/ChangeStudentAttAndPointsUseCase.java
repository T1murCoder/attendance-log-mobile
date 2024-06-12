package ru.technosopher.attendancelogapp.domain.attendance;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public class ChangeStudentAttAndPointsUseCase {

    private final AttendanceRepository repository;

    public ChangeStudentAttAndPointsUseCase(AttendanceRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String attId,
                        @NonNull String lessonId,
                        @NonNull String studentId,
                        @NonNull Boolean isVisited,
                        @NonNull String points,
                        Consumer<Status<Void>> callback){
        repository.changeStudentAttAndPoints(attId, lessonId, studentId, isVisited, points, callback);
    }
}
