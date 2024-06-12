package ru.technosopher.attendancelogapp.data.source;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.technosopher.attendancelogapp.data.dto.QrCodeDto;

public interface QrCodeApi {

    @POST("qrcode/")
    Call<QrCodeDto> checkQrCodeIsAlive(@Body QrCodeDto dto);
}
