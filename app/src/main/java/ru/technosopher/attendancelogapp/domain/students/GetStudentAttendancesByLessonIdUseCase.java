package ru.technosopher.attendancelogapp.domain.students;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetStudentAttendancesByLessonIdUseCase {
    private final StudentRepository repository;

    public GetStudentAttendancesByLessonIdUseCase(StudentRepository repository) {
        this.repository = repository;
    }
    public void execute(@NonNull String id, Consumer<Status<List<ItemStudentEntity>>> callback){
        repository.getStudentAttendancesByLessonId(id, callback);
    }
}
