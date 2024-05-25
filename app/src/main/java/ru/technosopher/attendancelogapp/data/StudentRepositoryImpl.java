package ru.technosopher.attendancelogapp.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;
import ru.technosopher.attendancelogapp.data.dto.StudentDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.StudentApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.data.utils.Mapper;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;
import ru.technosopher.attendancelogapp.domain.students.StudentRepository;

public class StudentRepositoryImpl implements StudentRepository {

    private static StudentRepositoryImpl INSTANCE;

    private final StudentApi studentApi = RetrofitFactory.getInstance().getStudentApi();

    public static StudentRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StudentRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getAllStudents(@NonNull Consumer<Status<List<ItemStudentEntity>>> callback) {
        studentApi.getAllStudents().enqueue(new CallToConsumer<>(
                callback,
                studentItems -> {
                    if (studentItems != null) {
                        ArrayList<ItemStudentEntity> res = new ArrayList<>();
                        for (StudentItemDto dto : studentItems) {
                            final String id = dto.id;
                            final String name = dto.name;
                            if (id != null && name != null) {
                                res.add(new ItemStudentEntity(id, name));
                            }
                        }
                        return res;

                    }
                    return null;
                }
        ));
    }

    @Override
    public void getStudentsAttendances(@NonNull String groupId, Consumer<Status<List<StudentEntity>>> callback) {
        studentApi.getStudentWithAttendancesByGroupId(groupId).enqueue(new CallToConsumer<>(
                callback,
                studentsDto -> {
                    if (studentsDto != null){
                        ArrayList<StudentEntity> res = new ArrayList<>();
                        for (StudentDto dto: studentsDto){
                            final String id = dto.id;
                            final String name = dto.name;
                            final String surname = dto.surname;
                            final String points = dto.points;
                            List<AttendanceEntity> attendanceEntityList = Mapper.fromAttendanceDtoToAttendanceEntityList(dto.attendanceDtoList);
                            if (id != null && name != null && surname != null){
                                //TODO(points might be null. Fix this)
                                res.add(new StudentEntity(id, name, surname, "0", attendanceEntityList));
                            }
                        }
                        return res;
                    }
                    return null;
                }
        ));
    }

    @Override
    public void getStudentAttendancesByLessonId(@NonNull String id, Consumer<Status<List<ItemStudentEntity>>> callback) {
        studentApi.getStudentWithAttendancesByLessonId(id).enqueue(new CallToConsumer<>(
                callback,
                studentItems -> {
                    if (studentItems != null) {
                        ArrayList<ItemStudentEntity> res = new ArrayList<>();
                        for (StudentItemDto dto : studentItems) {
                            final String name = dto.name;
                            final String surname = dto.surname;
                            if (name != null && surname != null) {
                                res.add(new ItemStudentEntity(id, name+" "+surname));
                            }
                        }
                        return res;

                    }
                    return null;
                }
        ));
    }
}
