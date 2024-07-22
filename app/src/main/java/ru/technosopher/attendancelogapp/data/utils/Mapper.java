package ru.technosopher.attendancelogapp.data.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import ru.technosopher.attendancelogapp.data.dto.AttendanceDto;
import ru.technosopher.attendancelogapp.data.dto.QrCodeDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.data.dto.TeacherDto;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.ui.utils.ItemStudentEntityModel;

public class Mapper {
    public static StudentItemDto fromEntityToDto(@NonNull ItemStudentEntity entity){
        StudentItemDto dto = new StudentItemDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        return dto;
    }

    public static List<StudentItemDto> fromEntityListToDtoList(@NonNull List<ItemStudentEntity> entityList){
        List<StudentItemDto> dtoList = new ArrayList<>();
        for (ItemStudentEntity entity: entityList) {
            dtoList.add(fromEntityToDto(entity));
        }
        return dtoList;
    }

    public static ItemStudentEntity fromDtoToEntity(@NonNull StudentItemDto dto){
        if (dto.id == null || dto.name == null || dto.surname == null || dto.username == null) return new ItemStudentEntity("", "", "", "");
        return new ItemStudentEntity(dto.id, dto.name, dto.surname, dto.username);
    }

    public static List<ItemStudentEntity> fromDtoListToEntityList(@NonNull List<StudentItemDto> dtoList){
        List<ItemStudentEntity> entityList = new ArrayList<>();
        for (StudentItemDto dto: dtoList) {
            entityList.add(fromDtoToEntity(dto));
        }
        return entityList;
    }

    public static List<AttendanceEntity> fromAttendanceDtoToAttendanceEntityList(@NonNull List<AttendanceDto> dtoList){
        List<AttendanceEntity> entityList = new ArrayList<>();
        for (AttendanceDto dto: dtoList) {
            entityList.add(fromAttendanceDtoToAttendanceEntity(dto));
        }
        return entityList;
    }

    private static AttendanceEntity fromAttendanceDtoToAttendanceEntity(AttendanceDto dto) {
        if (dto.id == null || dto.isVisited == null || dto.lessonId == null || dto.studentId == null || dto.lessonTimeStart == null || dto.points == null) return new AttendanceEntity("", false, "", "", new GregorianCalendar(), "0");
        return new AttendanceEntity(dto.id, dto.isVisited, dto.studentId, dto.lessonId, dto.lessonTimeStart, dto.points);
    }

    public static QrCodeEntity fromQrDtoToEntity(QrCodeDto qrCodeDto){
        if (qrCodeDto != null){
            final String id = qrCodeDto.id;
            final String _lessonId = qrCodeDto.lessonId;
            final GregorianCalendar createdAt = qrCodeDto.createdAt;
            final GregorianCalendar expiresAt = qrCodeDto.expiresAt;
            if (id != null && _lessonId != null && createdAt != null && expiresAt != null){
                return new QrCodeEntity(id, _lessonId, createdAt, expiresAt);
            }
        }
        return null;
    }

    public static TeacherDto fromTeacherEntityToDto(@NonNull TeacherEntity entity){
        return new TeacherDto(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getUsername(),
                entity.getTelegram_url(),
                entity.getGithub_url(),
                entity.getPhoto_url());
    }

    public static TeacherEntity fromTeacherDtoToEntity(@NonNull TeacherDto dto){
        if (dto == null) return null;
        if (dto.id == null || dto.name == null || dto.surname == null || dto.username == null) return null;
        return new TeacherEntity(dto.id, dto.name, dto.surname, dto.username, dto.telegram_url, dto.github_url, dto.photo_url);
    }

    public static ItemStudentEntityModel fromEntityToModel(ItemStudentEntity entity) {
        return new ItemStudentEntityModel(entity.getId(), entity, false);
    }

    public static ItemStudentEntity fromModelToEntity(ItemStudentEntityModel selectedState) {
        return new ItemStudentEntity(selectedState.getId(), "", "", "");
    }
}
