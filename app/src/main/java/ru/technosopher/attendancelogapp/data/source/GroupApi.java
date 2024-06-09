package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.dto.GroupDto;
import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;

public interface GroupApi {
    @GET("group/teacher/")
    Call<List<GroupWithoutStudentsDto>> getGroups();
    @POST("group/")
    Call<GroupDto> addGroup(@Body GroupDto dto);
    @DELETE("group/{id}")
    Call<Void> deleteGroup(@Path("id") String id);
    @GET("group/{id}")
    Call<GroupDto> getGroupNameById(@Path("id") String id);
    @PUT("group/{id}")
    Call<Void> addStudentsToGroup(@Path("id") String id, @Body GroupDto dto);
    @PUT("group/{id}/student/")
    Call<Void> putStudentsToGroup(@Path("id") String id, @Body List<StudentItemDto> dto);
    @DELETE("group/{groupId}/student/{studentId}")
    Call<Void> deleteStudentFromGroup(@Path("groupId") String groupId, @Path("studentId") String studentId);
}
