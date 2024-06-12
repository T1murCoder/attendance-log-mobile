package ru.technosopher.attendancelogapp.domain.groups;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class GetGroupsListUseCase {

    private final GroupsRepository repository;

    public GetGroupsListUseCase(GroupsRepository repository) {
        this.repository = repository;
    }

    public void execute(Consumer<Status<List<ItemGroupEntity>>> callback){
        repository.getGroups(callback);
    }
}
