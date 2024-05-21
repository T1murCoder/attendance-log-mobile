package ru.technosopher.attendancelogapp.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.LessonApi;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.lessons.LessonRepository;

public class LessonRepositoryImpl implements LessonRepository {

    private static LessonRepositoryImpl INSTANCE;

    private LessonApi lessonApi = RetrofitFactory.getInstance().getLessonApi();

    public static LessonRepositoryImpl getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new LessonRepositoryImpl();
        return INSTANCE;
    }

    @Override
    public void getAllLessons(@NonNull Consumer<Status<List<LessonEntity>>> callback) {

    }

    @Override
    public void getLessonById(@NonNull String id, @NonNull Consumer<Status<LessonEntity>> callback) {

    }
}
