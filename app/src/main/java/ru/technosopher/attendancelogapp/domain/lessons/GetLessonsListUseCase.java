package ru.technosopher.attendancelogapp.domain.lessons;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetLessonsListUseCase {

    private final LessonRepository repository;

    public GetLessonsListUseCase(LessonRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull Consumer<Status<List<LessonEntity>>> callback){
        repository.getAllLessons(callback);
    }
}
