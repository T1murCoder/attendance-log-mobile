package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.dto.StudentDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;

public interface StudentApi {
    @GET("student/vacant/")
    Call<List<StudentItemDto>> getAllStudents();

    @GET("student/group/{id}")
    Call<List<StudentDto>> getStudentsAttendances(@Path("id") String id);
}
