package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.GregorianCalendar;

public class LessonDto {

    @Nullable
    public String id;
    @Nullable
    public String theme;
    @Nullable
    public String groupId;
    @Nullable
    public GregorianCalendar timeStart;
    @Nullable
    public GregorianCalendar timeEnd;
    @Nullable
    public GregorianCalendar date;

}
