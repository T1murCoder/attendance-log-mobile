package ru.technosopher.attendancelogapp.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.StudentApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.students.StudentRepository;

public class StudentRepositoryImpl implements StudentRepository {

    private static StudentRepositoryImpl INSTANCE;

    private final StudentApi studentApi = RetrofitFactory.getInstance().getStudentApi();

    public static StudentRepositoryImpl getInstance() {
        if (INSTANCE == null){
            INSTANCE = new StudentRepositoryImpl();
        }
        return INSTANCE;
    }
    @Override
    public void getAllStudents(@NonNull Consumer<Status<List<ItemStudentEntity>>> callback) {
        studentApi.getAllStudents().enqueue(new CallToConsumer<>(
                callback,
                studentItems -> {
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
        ));
    }
}
