package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.technosopher.attendancelogapp.data.dto.LessonDto;

public interface LessonApi {

    @GET("")
    Call<List<LessonDto>> getAllLessons();
}
