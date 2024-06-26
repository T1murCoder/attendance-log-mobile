package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.data.dto.TeacherAccountDto;
import ru.technosopher.attendancelogapp.data.dto.TeacherDto;
import ru.technosopher.attendancelogapp.data.dto.TeacherRegisterDto;
import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;

public interface TeacherApi {
    @GET("teacher/login")
    Call<TeacherDto> login();
    @GET("teacher/username/{username}")
    Call<Void> isExists(@Path("username") String login);
    @POST("teacher/register")
    Call<TeacherAccountDto> register(@Body TeacherRegisterDto dto);
    @PUT("teacher/{id}")
    Call<TeacherDto> update(@Path("id") String id, @Body TeacherDto dto);
}
