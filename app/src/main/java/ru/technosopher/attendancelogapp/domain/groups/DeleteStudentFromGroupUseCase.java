package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;

public class DeleteStudentFromGroupUseCase {
    private final GroupsRepository repository;

    public DeleteStudentFromGroupUseCase(GroupsRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String groupId, @NonNull String studentId, Consumer<Status<Void>> callback){
        repository.deleteStudentFromGroup(groupId, studentId, callback);
    }
}
