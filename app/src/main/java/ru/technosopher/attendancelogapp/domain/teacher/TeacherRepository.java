package ru.technosopher.attendancelogapp.domain.teacher;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherAccountEntity;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public interface TeacherRepository {
    void getAllTechers(@NonNull Consumer<Status<List<ItemTeacherEntity>>> callback);
    void getTeacher(@NonNull String id, Consumer<Status<TeacherEntity>> callback);
    void updateProfile(@NonNull String id, @NonNull TeacherEntity updatedTeacher, Consumer<Status<TeacherEntity>> callback);
}
