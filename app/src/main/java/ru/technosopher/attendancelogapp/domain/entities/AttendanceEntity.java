package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class AttendanceEntity {

    @NonNull
    private final String id;

    @NonNull
    private final Boolean isVisited;

    @NonNull
    private final String studentId;

    @NonNull
    private final String lessonId;

    public AttendanceEntity(@NonNull String id, @NonNull Boolean isVisited, @NonNull String studentId, @NonNull String lessonId) {
        this.id = id;
        this.isVisited = isVisited;
        this.studentId = studentId;
        this.lessonId = lessonId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public Boolean getVisited() {
        return isVisited;
    }

    @NonNull
    public String getStudentId() {
        return studentId;
    }

    @NonNull
    public String getLessonId() {
        return lessonId;
    }
}
