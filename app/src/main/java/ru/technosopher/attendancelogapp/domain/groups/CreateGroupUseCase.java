package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.GroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;

import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class CreateGroupUseCase {

    private final GroupsRepository repository;

    public CreateGroupUseCase(GroupsRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String name, @NonNull List<ItemStudentEntity> students, Consumer<Status<GroupEntity>> callback) {
        repository.addGroup(name, students, callback);
    }
}
