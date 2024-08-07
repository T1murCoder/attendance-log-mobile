package ru.technosopher.attendancelogapp.domain.groups;

import android.content.ClipData;

import androidx.annotation.NonNull;

import java.util.List;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.domain.entities.GroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public interface GroupsRepository {

    void getGroups(@NonNull Consumer<Status<List<ItemGroupEntity>>> callback);
    void getGroupById(@NonNull String id, Consumer<Status<GroupEntity>> callback);
    void addGroup(@NonNull String name, @NonNull List<ItemStudentEntity> students, @NonNull Consumer<Status<GroupEntity>> callback);
    void deleteGroup(@NonNull String id, @NonNull Consumer<Status<Void>> callback);
    void addStudentsToGroup(@NonNull String id, @NonNull List<ItemStudentEntity> students, Consumer<Status<Void>> callback);
    void putStudentsToGroup(@NonNull String id, @NonNull List<ItemStudentEntity> students, Consumer<Status<Void>> callback);
    void deleteStudentFromGroup(@NonNull String groupId, @NonNull String studentId, Consumer<Status<Void>> callback);
}
