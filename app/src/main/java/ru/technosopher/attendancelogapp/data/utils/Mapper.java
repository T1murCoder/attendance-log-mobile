package ru.technosopher.attendancelogapp.data.utils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.technosopher.attendancelogapp.data.dto.AttendanceDto;
import ru.technosopher.attendancelogapp.data.dto.StudentDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;

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
        if (dto.id == null || dto.name == null) return new ItemStudentEntity("", "");
        return new ItemStudentEntity(dto.id, dto.name);
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
        if (dto.id == null || dto.isVisited == null || dto.lessonId == null || dto.studentId == null) return new AttendanceEntity("", false, "", "");
        return new AttendanceEntity(dto.id, dto.isVisited, dto.studentId, dto.lessonId);
    }

}
