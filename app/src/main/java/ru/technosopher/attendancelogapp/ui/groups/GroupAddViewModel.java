package ru.technosopher.attendancelogapp.ui.groups;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.technosopher.attendancelogapp.data.StudentRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.students.GetStudentsListUseCase;

public class GroupAddViewModel extends ViewModel {

    /* LIVEDATA */

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<State>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    /* USE CASES */

    GetStudentsListUseCase getStudentsListUseCase = new GetStudentsListUseCase(
            StudentRepositoryImpl.getInstance()
    );
    /* LOGIC */

    public void load() {
        getStudentsListUseCase.execute(status -> {
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<ItemStudentEntity>> status) {
        return new State(
                status.getValue(),
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getErrors() == null && status.getValue() != null);
    }


    public class State {

        @Nullable
        private final List<ItemStudentEntity> students;
        @Nullable
        private final String errorMessage;
        @NonNull
        private final Boolean isSuccess;

        public State(@Nullable List<ItemStudentEntity> students, @Nullable String errorMessage, @NonNull Boolean isSuccess) {
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
        }

        @Nullable
        public List<ItemStudentEntity> getStudents() {
            return students;
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
