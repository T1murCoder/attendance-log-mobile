package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.GregorianCalendar;

public class LessonEntity {
    @NonNull
    private final String id;
    @NonNull
    private final String theme;
    @NonNull
    private final String groupId;
    @NonNull
    private String groupName;
    @NonNull
    private final GregorianCalendar timeStart;
    @NonNull
    private final GregorianCalendar timeEnd;
    @NonNull
    private final GregorianCalendar date;
    @Nullable
    private QrCodeEntity activeQrCode;

    public  LessonEntity(@NonNull String id, @NonNull String theme, @NonNull String groupId, @NonNull String groupName, @NonNull GregorianCalendar timeStart, @NonNull GregorianCalendar timeEnd, @NonNull GregorianCalendar date, @Nullable QrCodeEntity activeQrCode){
        this.id = id;
        this.theme = theme;
        this.groupId = groupId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.date = date;
        this.groupName = groupName;
        this.activeQrCode = activeQrCode;
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
    @NonNull
    public String getGroupName() {
        return groupName;
    }
    @Nullable
    public QrCodeEntity getActiveQrCode() {
        return activeQrCode;
    }

    public void setActiveQrCode(@NonNull QrCodeEntity qrCodeEntity){
        this.activeQrCode = qrCodeEntity;
    }
}
