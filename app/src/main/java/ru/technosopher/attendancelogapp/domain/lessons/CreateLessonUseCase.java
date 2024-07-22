package ru.technosopher.attendancelogapp.domain.lessons;

import androidx.annotation.NonNull;

import java.util.GregorianCalendar;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class CreateLessonUseCase {

    private final LessonRepository repository;

    public CreateLessonUseCase(LessonRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String theme,
                        @NonNull String groupId,
                        @NonNull String groupName,
                        @NonNull GregorianCalendar timeStart,
                        @NonNull GregorianCalendar timeEnd,
                        @NonNull GregorianCalendar date, Consumer<Status<LessonEntity>> callback){
        repository.createLesson(theme, groupId, groupName, timeStart, timeEnd, date, callback);
    }
}
