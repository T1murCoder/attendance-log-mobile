package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.dto.LessonDto;

public interface LessonApi {
    @GET("lesson/teacher/")
    Call<List<LessonDto>> getAllLessons();

    @POST("lesson/")
    Call<LessonDto> createLesson(@Body LessonDto dto);

    @DELETE("lesson/{id}")
    Call<Void> deleteLesson(@Path("id") String id);
}
