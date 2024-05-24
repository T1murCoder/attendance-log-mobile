package ru.technosopher.attendancelogapp.ui.table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.technosopher.attendancelogapp.data.StudentRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;
import ru.technosopher.attendancelogapp.domain.students.GetStudentsAttendancesUseCase;
import ru.technosopher.attendancelogapp.ui.group_add.GroupAddViewModel;

public class TableViewModel extends ViewModel {

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<State>();
    public LiveData<State> stateLiveData = mutableStateLiveData;

    /* USE CASES */

    private GetStudentsAttendancesUseCase getStudentsAttendancesUseCase = new GetStudentsAttendancesUseCase(
            StudentRepositoryImpl.getInstance()
    );

    /* USE CASES */

    public void update(@NonNull String id){
        mutableStateLiveData.postValue(new State(null, null, false, true));
        getStudentsAttendancesUseCase.execute(id, status -> {
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<StudentEntity>> status) {
        return new State(
                status.getValue(),
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getErrors() == null && status.getValue() != null, false);
    }


    public class State{

        @Nullable
        private final List<StudentEntity> students;

        @Nullable
        private final String errorMessage;

        @NonNull
        private final Boolean isSuccess;

        @NonNull
        private final Boolean isLoading;

        public State(@Nullable List<StudentEntity> students, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<StudentEntity> getStudents() {
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

        @NonNull
        public Boolean getLoading() {
            return isLoading;
        }
    }
}
