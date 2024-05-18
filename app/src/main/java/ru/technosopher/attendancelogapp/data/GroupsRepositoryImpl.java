package ru.technosopher.attendancelogapp.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.GroupWithoutStudentsDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.GroupApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
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
    public void getGroupById() {

    }
}
