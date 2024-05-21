package ru.technosopher.attendancelogapp.domain.lessons;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface LessonRepository {
    void getAllLessons(@NonNull Consumer<Status<List<LessonEntity>>> callback);
    void getLessonById(@NonNull String id, @NonNull Consumer<Status<LessonEntity>> callback);

}
