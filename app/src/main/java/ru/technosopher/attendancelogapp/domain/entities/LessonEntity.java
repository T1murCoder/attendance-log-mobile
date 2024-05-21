package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

import java.util.GregorianCalendar;

public class LessonEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String theme;
    @NonNull
    private final String groupId;
    @NonNull
    private final GregorianCalendar timeStart;
    @NonNull
    private final GregorianCalendar timeEnd;
    @NonNull
    private final GregorianCalendar date;

    public LessonEntity(@NonNull String id, @NonNull String theme, @NonNull String groupId, @NonNull GregorianCalendar timeStart, @NonNull GregorianCalendar timeEnd, @NonNull GregorianCalendar date) {
        this.id = id;
        this.theme = theme;
        this.groupId = groupId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.date = date;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTheme() {
        return theme;
    }

    @NonNull
    public String getGroupId() {
        return groupId;
    }

    @NonNull
    public GregorianCalendar getTimeStart() {
        return timeStart;
    }

    @NonNull
    public GregorianCalendar getTimeEnd() {
        return timeEnd;
    }

    @NonNull
    public GregorianCalendar getDate() {
        return date;
    }
}
