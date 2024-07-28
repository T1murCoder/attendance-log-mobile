package ru.technosopher.attendancelogapp.domain.join;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.RequestEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetGroupJoinRequestsUseCase {

    private final JoinRepository repository;

    public GetGroupJoinRequestsUseCase(JoinRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, @NonNull Consumer<Status<List<RequestEntity>>> callback){
        repository.getRequests(id, callback);
    }
}
