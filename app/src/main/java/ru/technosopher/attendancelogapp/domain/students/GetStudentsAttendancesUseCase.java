package ru.technosopher.attendancelogapp.domain.students;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;

public class GetStudentsAttendancesUseCase {
    private final StudentRepository repository;

    public GetStudentsAttendancesUseCase(StudentRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, Consumer<Status<List<StudentEntity>>> callback){
        repository.getStudentsAttendances(id, callback);
    }
}
