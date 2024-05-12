package ru.technosopher.attendancelogapp.domain;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public class GetTeachersListUseCase {

    private final TeacherRepository repository;

    public GetTeachersListUseCase(TeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull Consumer<Status<List<ItemTeacherEntity>>> callback) {
        repository.getAllTechers(callback);
    }
}

