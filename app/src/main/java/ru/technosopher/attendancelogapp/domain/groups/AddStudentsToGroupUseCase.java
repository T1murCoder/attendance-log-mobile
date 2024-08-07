package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class AddStudentsToGroupUseCase {

    private final GroupsRepository repository;

    public AddStudentsToGroupUseCase(GroupsRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String id, @NonNull List<ItemStudentEntity> students, Consumer<Status<Void>> callback){
        repository.addStudentsToGroup(id, students, callback);
    }
}
