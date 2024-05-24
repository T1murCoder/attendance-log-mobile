package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;
import androidx.annotation.NonUiContext;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.technosopher.attendancelogapp.data.dto.AttendanceDto;

public class StudentEntity {

    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String surname;
    @NonNull
    private final String points;
    @NonNull
    private final List<AttendanceEntity> attendanceEntityList;

    public StudentEntity(@NonNull String id, @NonNull String name, @NonNull String surname, @NonNull String points, @NonNull List<AttendanceEntity> attendanceEntityList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.points = points;
        this.attendanceEntityList = attendanceEntityList;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getSurname() {
        return surname;
    }

    @NonNull
    public String getPoints() {
        return points;
    }
    @NonNull
    public List<AttendanceEntity> getAttendanceEntityList() {
        return attendanceEntityList;
    }
}
