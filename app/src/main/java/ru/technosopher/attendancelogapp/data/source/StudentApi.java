package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.dto.StudentDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.data.dto.StudentWithAttendancesDto;

public interface StudentApi {
    @GET("student/vacant/")
    Call<List<StudentItemDto>> getAllStudents();

    @GET("student/group/{id}")
    Call<List<StudentWithAttendancesDto>> getStudentWithAttendancesByGroupId(@Path("id") String id);

    @GET("student/lesson/{id}")
    Call<List<StudentDto>> getStudentWithAttendancesByLessonId(@Path("id") String id);
}
