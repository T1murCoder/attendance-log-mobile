package ru.technosopher.attendancelogapp.domain.students;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetStudentsListUseCase {
    private final StudentRepository repository;

    public GetStudentsListUseCase(StudentRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull Consumer<Status<List<ItemStudentEntity>>> callback){
        repository.getAllStudents(callback);
    }
}
