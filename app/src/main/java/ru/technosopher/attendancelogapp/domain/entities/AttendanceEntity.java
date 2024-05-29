package ru.technosopher.attendancelogapp.domain.entities;

import androidx.annotation.NonNull;

import java.util.Comparator;
import java.util.GregorianCalendar;

public class AttendanceEntity implements Comparable<AttendanceEntity> {

    @NonNull
    private final String id;

    @NonNull
    private final Boolean isVisited;

    @NonNull
    private final String studentId;

    @NonNull
    private final String lessonId;

    @NonNull
    private final GregorianCalendar lessonTimeStart;



    @NonNull
    private final String points;

    public AttendanceEntity(@NonNull String id, @NonNull Boolean isVisited, @NonNull String studentId, @NonNull String lessonId, @NonNull GregorianCalendar lessonTimeStart, @NonNull String points) {
        this.id = id;
        this.isVisited = isVisited;
        this.studentId = studentId;
        this.lessonId = lessonId;
        this.lessonTimeStart = lessonTimeStart;
        this.points = points;
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

    @NonNull
    public GregorianCalendar getLessonTimeStart() {
        return lessonTimeStart;
    }


    @NonNull
    public String getPoints() {
        return points;
    }
    @Override
    public int compareTo(AttendanceEntity attendanceEntity) {
        if (getLessonTimeStart().compareTo(attendanceEntity.getLessonTimeStart()) > 0){
            return 1;
        }
        else if (getLessonTimeStart().compareTo(attendanceEntity.getLessonTimeStart()) == 0){
            return 0;
        }
        else{
            return -1;
        }
    }
}
