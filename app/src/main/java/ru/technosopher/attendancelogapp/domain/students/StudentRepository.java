package ru.technosopher.attendancelogapp.domain.students;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public interface StudentRepository {

    //void getStudentById(@NonNull String id, Consumer<Status<ItemStudentEntity>> callback);
    void getAllStudents(@NonNull Consumer<Status<List<ItemStudentEntity>>> callback);
}
