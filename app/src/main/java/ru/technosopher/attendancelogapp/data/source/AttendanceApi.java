package ru.technosopher.attendancelogapp.data.source;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.dto.AttendanceDto;

public interface AttendanceApi {
    @PUT("attendance/{id}")
    Call<Void> setAttendanceAndPointsToStudent(@Path("id") String id, @Body AttendanceDto dto);
}
