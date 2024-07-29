package ru.technosopher.attendancelogapp.data.repository;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.RequestDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.JoinApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.RequestEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;
import ru.technosopher.attendancelogapp.domain.join.JoinRepository;

public class JoinRepositoryImpl implements JoinRepository {

    public static JoinRepositoryImpl INSTANCE;

    private final JoinApi joinApi = RetrofitFactory.getInstance().getJoinApi();

    public static JoinRepositoryImpl getInstance() {
        if (INSTANCE == null) INSTANCE = new JoinRepositoryImpl();
        return INSTANCE;
    }


    @Override
    public void getRequests(@NonNull String id, @NonNull Consumer<Status<List<RequestEntity>>> callback) {
        joinApi.getGroupRequests(id).enqueue(new CallToConsumer<>(
                callback,
                requestsDto -> {
                    if (requestsDto != null){
                        ArrayList<RequestEntity> res = new ArrayList<>();
                        for (RequestDto dto: requestsDto) {
                            if (dto.id != null && dto.student != null){
                                if (dto.student.id != null && dto.student.name != null && dto.student.surname != null && dto.student.username != null && dto.student.points != null){
                                    res.add(new RequestEntity(dto.id, new ItemStudentEntity(dto.student.id, dto.student.name, dto.student.surname, dto.student.username)));
                                }
                            }
                        }
                        return res;
                    }
                    return null;
                }
        ));
    }

    @Override
    public void acceptStudent(@NonNull String id, @NonNull Consumer<Status<Void>> callback) {
        joinApi.accept(id).enqueue(new CallToConsumer<>(
                callback,
                dto -> null)
        );
    }

    @Override
    public void declineStudent(@NonNull String id, @NonNull Consumer<Status<Void>> callback) {
        joinApi.decline(id).enqueue(new CallToConsumer<>(
                callback,
                dto -> null)
        );
    }
}
