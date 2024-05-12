package ru.technosopher.attendancelogapp.domain;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public interface TeacherRepository {

    //TODO(rework requests)

    void getAllTechers(@NonNull Consumer<Status<List<ItemTeacherEntity>>> callback);

    void getTeacher(@NonNull String id, Consumer<Status<TeacherEntity>> callback);
}
