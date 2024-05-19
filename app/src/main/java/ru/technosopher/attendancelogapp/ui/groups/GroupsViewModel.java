package ru.technosopher.attendancelogapp.ui.groups;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.technosopher.attendancelogapp.data.GroupsRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.groups.DeleteGroupUseCase;
import ru.technosopher.attendancelogapp.domain.groups.GetGroupsListUseCase;
import ru.technosopher.attendancelogapp.domain.groups.GroupsRepository;

public class GroupsViewModel extends ViewModel {

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<String> mutableDeleteLiveData = new MutableLiveData<>();
    public final LiveData<String> deleteLiveData = mutableDeleteLiveData;

    /* USE CASES */
    private final GetGroupsListUseCase getGroupsListUseCase = new GetGroupsListUseCase(GroupsRepositoryImpl.getInstance());

    private final DeleteGroupUseCase deleteGroupUseCase = new DeleteGroupUseCase(GroupsRepositoryImpl.getInstance());
    /* USE CASES */

    public GroupsViewModel() {
        update();
    }

    public void update() {
        getGroupsListUseCase.execute(status -> {
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<ItemGroupEntity>> status) {
        return new State(
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getValue(),
                status.getErrors() == null && status.getValue() != null);
    }

    public void deleteGroup(@NonNull String id){
        deleteGroupUseCase.execute(id, status -> {
            if (status.getStatusCode() == 200 && status.getErrors() == null){
                mutableDeleteLiveData.postValue("Группа успешно удалена.");
                return ;
            }
            if(status.getStatusCode() != 200 || status.getErrors() != null){
                mutableDeleteLiveData.postValue("Что-то пошло не так.");
                return ;
            }
        });
    }

    public class State {
        @Nullable
        private final List<ItemGroupEntity> groups;
        @Nullable
        private final String errorMessage;

        @NonNull
        private final Boolean isSuccess;

        public State(@Nullable String errorMessage, @Nullable List<ItemGroupEntity> groups, @NonNull Boolean isSuccess) {
            this.groups = groups;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
        }

        @Nullable
        public List<ItemGroupEntity> getGroups() {
            return groups;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @NonNull
        public Boolean getSuccess() {
            return isSuccess;
        }
    }
}
