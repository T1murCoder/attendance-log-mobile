package ru.technosopher.attendancelogapp.domain.join;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.RequestEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface JoinRepository {

    void getRequests(@NonNull String id, @NonNull Consumer<Status<List<RequestEntity>>> callback);

    void acceptStudent(@NonNull String id, @NonNull Consumer<Status<Void>> callback);

    void declineStudent(@NonNull String id, @NonNull Consumer<Status<Void>> callback);

}
