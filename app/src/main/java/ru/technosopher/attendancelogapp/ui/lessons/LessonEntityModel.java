package ru.technosopher.attendancelogapp.ui.lessons;

import java.util.GregorianCalendar;

import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;

public class LessonEntityModel {

    private LessonEntity lesson;

    private Boolean closed;

    public LessonEntityModel(LessonEntity lesson, Boolean closed) {
        this.lesson = lesson;
        this.closed = closed;
    }
    public LessonEntity getLesson() {
        return lesson;
    }

    public Boolean isClosed() {
        return closed;
    }

    public void setLesson(LessonEntity lesson) {
        this.lesson = lesson;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public GregorianCalendar getTimeStart(){
        return getLesson().getTimeStart();
    }
}
