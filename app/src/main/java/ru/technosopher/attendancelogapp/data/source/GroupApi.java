package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;

public interface GroupApi {

    @GET("group/teacher/")
    Call<List<GroupWithoutStudentsDto>> getGroups();
}
