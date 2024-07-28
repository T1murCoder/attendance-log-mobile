package ru.technosopher.attendancelogapp.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.technosopher.attendancelogapp.data.dto.RequestDto;

public interface JoinApi {
    @GET("/join_request/group/{group_id}")
    Call<List<RequestDto>> getGroupRequests(@Path("group_id") String groupId);

    @PUT("/join_request/{request_id}/accept")
    Call<Void> accept(@Path("request_id") String id);

    @PUT("/join_request/{request_id}/teacher/decline")
    Call<Void> decline(@Path("request_id") String id);
}
