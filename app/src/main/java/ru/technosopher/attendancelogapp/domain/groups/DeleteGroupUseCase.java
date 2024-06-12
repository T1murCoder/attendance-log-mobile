package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public class DeleteGroupUseCase {

    private final GroupsRepository repository;

    public DeleteGroupUseCase(GroupsRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, Consumer<Status<Void>> callback){
        repository.deleteGroup(id, callback);
    }
}
