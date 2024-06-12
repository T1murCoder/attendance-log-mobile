package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetGroupNameByIdUseCase {
    private final GroupsRepository repository;

    public GetGroupNameByIdUseCase(GroupsRepository repository) {
        this.repository = repository;
    }
    public void execute(@NonNull String id, Consumer<Status<String>> callback){
        repository.getGroupNameById(id, callback);
    }
}

