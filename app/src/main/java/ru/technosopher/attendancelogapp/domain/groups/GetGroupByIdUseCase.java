package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.GroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetGroupByIdUseCase {
    private final GroupsRepository repository;

    public GetGroupByIdUseCase(GroupsRepository repository) {
        this.repository = repository;
    }
    public void execute(@NonNull String id, Consumer<Status<GroupEntity>> callback){
        repository.getGroupById(id, callback);
    }
}

