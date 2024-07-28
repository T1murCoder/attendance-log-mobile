package ru.technosopher.attendancelogapp.data.repository;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.GroupDto;
import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;
import ru.technosopher.attendancelogapp.data.dto.StudentItemDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.GroupApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.data.utils.Mapper;
import ru.technosopher.attendancelogapp.domain.entities.GroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.groups.GroupsRepository;

public class GroupsRepositoryImpl implements GroupsRepository {

    private static GroupsRepositoryImpl INSTANCE;
    private GroupApi groupApi = RetrofitFactory.getInstance().getGroupApi();

    public static GroupsRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GroupsRepositoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public void getGroups(@NonNull Consumer<Status<List<ItemGroupEntity>>> callback) {
        groupApi.getGroups().enqueue(new CallToConsumer<>(
                callback,
                groupsDto -> {
                    ArrayList<ItemGroupEntity> res = new ArrayList<>();
                    for (GroupWithoutStudentsDto dto : groupsDto) {
                        final String id = dto.id;
                        final String name = dto.name;
                        if (id != null && name != null) {
                            res.add(new ItemGroupEntity(id, name));
                        }
                    }
                    return res;
                }));
    }

    @Override
    public void getGroupById(@NonNull String id, Consumer<Status<GroupEntity>> callback) {
        groupApi.getGroupNameById(id).enqueue(new CallToConsumer<>(
                callback,
                group -> {
                    if (group != null) {
                        if (group.id != null && group.name != null && group.joinCode != null) return new GroupEntity(group.name, group.id, new ArrayList<>(), group.joinCode);
                    }
                    return null;
                }
        ));
    }


    @Override
    public void addGroup(@NonNull String name, @NonNull List<ItemStudentEntity> students, @NonNull Consumer<Status<GroupEntity>> callback) {
        groupApi.addGroup(new GroupDto("0", name, Mapper.fromEntityListToDtoList(students))).enqueue(new CallToConsumer<>(
                callback,
                groupDto -> {
                    if (groupDto != null) {
                        if (groupDto.id != null && groupDto.name != null && groupDto.studentList != null && groupDto.joinCode != null) {
                            final String id = groupDto.id;
                            final String res_name = groupDto.name;
                            final List<StudentItemDto> res_students = groupDto.studentList;
                            final String joinCode = groupDto.joinCode;
                            return new GroupEntity(id, res_name, Mapper.fromDtoListToEntityList(res_students), joinCode);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void deleteGroup(@NonNull String id, @NonNull Consumer<Status<Void>> callback) {
        groupApi.deleteGroup(id).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void addStudentsToGroup(@NonNull String id, @NonNull List<ItemStudentEntity> students, Consumer<Status<Void>> callback) {
        groupApi.addStudentsToGroup(id, new GroupDto(id, "", Mapper.fromEntityListToDtoList(students))).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void putStudentsToGroup(@NonNull String id, @NonNull List<ItemStudentEntity> students, Consumer<Status<Void>> callback) {
        groupApi.putStudentsToGroup(id, Mapper.fromEntityListToDtoList(students)).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void deleteStudentFromGroup(@NonNull String groupId,  @NonNull String studentId, Consumer<Status<Void>> callback) {
        groupApi.deleteStudentFromGroup(groupId, studentId).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

}
