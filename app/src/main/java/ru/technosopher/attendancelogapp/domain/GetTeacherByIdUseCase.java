package ru.technosopher.attendancelogapp.domain;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public class GetTeacherByIdUseCase {

    private final TeacherRepository repository;

    public GetTeacherByIdUseCase(TeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, Consumer<Status<TeacherEntity>> callback) {
        repository.getTeacher(id, callback);
    }
}
