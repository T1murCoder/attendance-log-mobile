package ru.technosopher.attendancelogapp.domain.lessons;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public class DeleteLessonUseCase {

    private final LessonRepository repository;

    public DeleteLessonUseCase(LessonRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, Consumer<Status<Void>> callback){
        repository.deleteLesson(id, callback);
    }
}
