package ru.technosopher.attendancelogapp.domain.groups;

import androidx.annotation.NonNull;

import java.util.List;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface GroupsRepository {

    void getGroups(@NonNull Consumer<Status<List<ItemGroupEntity>>> callback);

    void getGroupById();
}
