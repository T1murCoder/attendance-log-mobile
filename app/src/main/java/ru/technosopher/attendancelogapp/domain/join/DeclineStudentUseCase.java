package ru.technosopher.attendancelogapp.domain.join;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public class DeclineStudentUseCase {
    private final JoinRepository repository;

    public DeclineStudentUseCase(JoinRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, @NonNull Consumer<Status<Void>> callback){
        repository.declineStudent(id, callback);
    }
}
