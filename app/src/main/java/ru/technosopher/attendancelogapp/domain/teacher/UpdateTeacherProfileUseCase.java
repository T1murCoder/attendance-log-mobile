package ru.technosopher.attendancelogapp.domain.teacher;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public class UpdateTeacherProfileUseCase {

    private final TeacherRepository repository;

    public UpdateTeacherProfileUseCase(TeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, @NonNull TeacherEntity updatedTeacher, Consumer<Status<TeacherEntity>> callback){
        repository.updateProfile(id, updatedTeacher, callback);
    }
}
