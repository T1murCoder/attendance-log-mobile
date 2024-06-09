package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.GregorianCalendar;

public class QrCodeDto {

    @Nullable
    @SerializedName("id")
    public String id;

    @Nullable
    @SerializedName("lessonId")
    public String lessonId;

    @Nullable
    @SerializedName("createdAt")
    public GregorianCalendar createdAt;

    @Nullable
    @SerializedName("expiresAt")
    public GregorianCalendar expiresAt;

    public QrCodeDto(@Nullable String id, @Nullable String lessonId, @Nullable GregorianCalendar createdAt, @Nullable GregorianCalendar expiresAt) {
        this.id = id;
        this.lessonId = lessonId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
