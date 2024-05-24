package ru.technosopher.attendancelogapp.data.dto;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.GregorianCalendar;

public class LessonDto {

    @Nullable
    @SerializedName("id")
    public String id;
    @Nullable
    @SerializedName("theme")
    public String theme;
    @Nullable
    @SerializedName("groupId")
    public String groupId;
    @Nullable
    @SerializedName("timeStart")
    public GregorianCalendar timeStart;
    @Nullable
    @SerializedName("timeEnd")
    public GregorianCalendar timeEnd;
    @Nullable
    @SerializedName("date")
    public GregorianCalendar date;

    @Nullable
    @SerializedName("groupName")
    public String groupName;

    public LessonDto(@Nullable String id, @Nullable String theme, @Nullable String groupId, @Nullable String groupName, @Nullable GregorianCalendar timeStart, @Nullable GregorianCalendar timeEnd, @Nullable GregorianCalendar date) {
        this.id = id;
        this.theme = theme;
        this.groupId = groupId;
        this.groupName = groupName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.date = date;
    }
}
