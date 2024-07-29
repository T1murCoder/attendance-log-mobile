package ru.technosopher.attendancelogapp.ui.group_manage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.technosopher.attendancelogapp.data.repository.GroupsRepositoryImpl;
import ru.technosopher.attendancelogapp.data.repository.JoinRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.RequestEntity;
import ru.technosopher.attendancelogapp.domain.groups.GetGroupByIdUseCase;
import ru.technosopher.attendancelogapp.domain.join.AcceptStudentUseCase;
import ru.technosopher.attendancelogapp.domain.join.DeclineStudentUseCase;
import ru.technosopher.attendancelogapp.domain.join.GetGroupJoinRequestsUseCase;

public class GroupManageViewModel extends ViewModel {
    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<State>();

    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<String> mutableInteractionLiveData = new MutableLiveData<>();

    public final LiveData<String> interactionLiveData = mutableInteractionLiveData;

    private final GetGroupByIdUseCase getGroupByIdUseCase = new GetGroupByIdUseCase(GroupsRepositoryImpl.getInstance());

    private final AcceptStudentUseCase acceptStudentUseCase = new AcceptStudentUseCase(JoinRepositoryImpl.getInstance());

    private final DeclineStudentUseCase declineStudentUseCase = new DeclineStudentUseCase(JoinRepositoryImpl.getInstance());

    private final GetGroupJoinRequestsUseCase getGroupJoinRequestsUseCase = new GetGroupJoinRequestsUseCase(JoinRepositoryImpl.getInstance());

    private String groupId;

    public void saveId(String id) {
        groupId = id;
    }

    public void update() {
        if (Objects.equals(groupId, "-1")){
            mutableStateLiveData.postValue(new State(
                    null,
                    "Что-то пошло не так. Попробуйте еще раз",
                    null,
                    false,
                    false
            ));
            return;
        }
        mutableStateLiveData.postValue(new State(null, null, null, true, false));
        getGroupByIdUseCase.execute(groupId, ges -> {
            if (ges.getStatusCode() == 200 && ges.getErrors() == null && ges.getValue() != null) {
                getGroupJoinRequestsUseCase.execute(groupId, requestsStatus -> {
                    mutableStateLiveData.postValue(new State(
                            requestsStatus.getStatusCode() == 200 && requestsStatus.getValue() != null ? requestsStatus.getValue() : new ArrayList<>(),
                            requestsStatus.getStatusCode() == 200 && requestsStatus.getErrors() == null ? null : "Не удалось загрузить заявки. Попробуйте еще раз",
                            ges.getValue().getJoinCode(),
                            false,
                            requestsStatus.getStatusCode() == 200 && requestsStatus.getValue() != null && requestsStatus.getErrors() == null
                    ));
                });
            } else {
                mutableStateLiveData.postValue(new State(
                        null,
                        "Что-то пошло не так. Попробуйте еще раз",
                        null,
                        false,
                        false
                ));
            }
        });
    }

    public void acceptStudent(@NonNull String id) {
        acceptStudentUseCase.execute(id, status -> {
            if (status.getStatusCode() == 200) mutableInteractionLiveData.postValue("Заявка успешно принята");
            else mutableInteractionLiveData.postValue("Не получилось добавить ученика");
        });
    }

    public void declineStudent(@NonNull String id) {
        declineStudentUseCase.execute(id, status -> {
            if (status.getStatusCode() == 200) mutableInteractionLiveData.postValue("Заявка успешно отклонена");
            else mutableInteractionLiveData.postValue("Не получилось отклонить заявку");
        });
    }

    public class State {

        @Nullable
        private final List<RequestEntity> requests;
        @Nullable
        private final String errorMsg;
        @Nullable
        private final String joinCode;
        @NonNull
        private final Boolean isLoading;
        @NonNull
        private final Boolean isSuccess;

        public State(@Nullable List<RequestEntity> requests, @Nullable String errorMsg, @Nullable String joinCode, @NonNull Boolean isLoading, @NonNull Boolean isSuccess) {
            this.requests = requests;
            this.errorMsg = errorMsg;
            this.joinCode = joinCode;
            this.isLoading = isLoading;
            this.isSuccess = isSuccess;
        }

        @Nullable
        public List<RequestEntity> getRequests() {
            return requests;
        }

        @Nullable
        public String getErrorMsg() {
            return errorMsg;
        }

        @NonNull
        public Boolean getLoading() {
            return isLoading;
        }

        @NonNull
        public Boolean getSuccess() {
            return isSuccess;
        }

        @Nullable
        public String getJoinCode() {
            return joinCode;
        }
    }
}
