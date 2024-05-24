package ru.technosopher.attendancelogapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;
import ru.technosopher.attendancelogapp.data.dto.LessonDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.LessonApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.lessons.LessonRepository;

public class LessonRepositoryImpl implements LessonRepository {

    private static LessonRepositoryImpl INSTANCE;

    private final LessonApi lessonApi = RetrofitFactory.getInstance().getLessonApi();

    public static LessonRepositoryImpl getINSTANCE() {
        if (INSTANCE == null) INSTANCE = new LessonRepositoryImpl();
        return INSTANCE;
    }

    @Override
    public void getAllLessons(@NonNull Consumer<Status<List<LessonEntity>>> callback) {
        lessonApi.getAllLessons().enqueue(new CallToConsumer<>(
                callback,
                lessonsDto -> {
//                    Log.e("LESSONS DTO", "something went wrong");
                    if (lessonsDto != null) {
                        ArrayList<LessonEntity> res = new ArrayList<>();
                        for (LessonDto dto : lessonsDto) {
                            final String id = dto.id;
                            final String theme = dto.theme;
                            final String groupId = dto.groupId;
                            final String groupName = dto.groupName;
                            final GregorianCalendar timeStart = dto.timeStart;
                            final GregorianCalendar timeEnd = dto.timeEnd;
                            final GregorianCalendar date = dto.date;
                            if (id != null && theme != null && groupId != null && groupName != null && timeStart != null && timeEnd != null && date != null) {
                                res.add(new LessonEntity(id, theme, groupId, groupName, timeStart, timeEnd, date));
                            }
                        }
                        return res;
                    }
                    return null;
                }));
    }

    @Override
    public void getLessonById(@NonNull String id, @NonNull Consumer<Status<LessonEntity>> callback) {

    }

    @Override
    public void createLesson(@NonNull String theme, @NonNull String groupId, @NonNull String groupName, @NonNull GregorianCalendar timeStart, @NonNull GregorianCalendar timeEnd, @NonNull GregorianCalendar date, Consumer<Status<LessonEntity>> callback) {
        lessonApi.createLesson(new LessonDto("0", theme, groupId, groupName, timeStart, timeEnd, date)).enqueue(new CallToConsumer<>(
                callback,
                lessonDto -> {
                    if (lessonDto != null) {
                        final String id = lessonDto.id;
                        final String _theme = lessonDto.theme;
                        final String _groupId = lessonDto.id;
                        final String _groupName = lessonDto.groupName;
                        final GregorianCalendar _timeStart = lessonDto.timeStart;
                        final GregorianCalendar _timeEnd = lessonDto.timeEnd;
                        final GregorianCalendar _date = lessonDto.date;
                        if (id != null && _theme != null && _groupId != null && _groupName != null && _timeStart != null && _timeEnd != null && _date != null) {
                            return new LessonEntity(id, theme, groupId, groupName, timeStart, timeEnd, date);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void deleteLesson(@NonNull String id, Consumer<Status<Void>> callback) {
        lessonApi.deleteLesson(id).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }
}
