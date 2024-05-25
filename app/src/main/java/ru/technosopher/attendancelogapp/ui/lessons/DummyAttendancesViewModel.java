package ru.technosopher.attendancelogapp.ui.lessons;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.technosopher.attendancelogapp.data.LessonRepositoryImpl;
import ru.technosopher.attendancelogapp.data.StudentRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.students.GetStudentAttendancesByLessonIdUseCase;
import ru.technosopher.attendancelogapp.ui.groups.GroupsViewModel;

public class DummyAttendancesViewModel extends ViewModel {
    private MutableLiveData<State> mutableStateLiveData = new MutableLiveData<State>();

    public LiveData<State> stateLiveData = mutableStateLiveData;

    GetStudentAttendancesByLessonIdUseCase getStudentAttendancesByLessonIdUseCase = new GetStudentAttendancesByLessonIdUseCase(
            StudentRepositoryImpl.getInstance()
    );

    public void load(@NonNull String id){
        mutableStateLiveData.postValue(new State(null, false, true));
        getStudentAttendancesByLessonIdUseCase.execute(id, status->{
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<ItemStudentEntity>> status) {
        System.out.println(status.getStatusCode());
        System.out.println(status.getValue());
        return new State(
                status.getValue(),
                status.getErrors() == null && status.getValue() != null, false);
    }

    public class State{
        @Nullable
        private final List<ItemStudentEntity> students;
        @NonNull
        private Boolean isSuccess;
        @NonNull
        private Boolean isLoading;

        public State(@Nullable List<ItemStudentEntity> students, @Nullable Boolean isSuccess, @NonNull Boolean isLoading) {
            this.students = students;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<ItemStudentEntity> getStudents() {
            return students;
        }

        @NonNull
        public Boolean getLoading() {
            return isLoading;
        }

        @NonNull
        public Boolean getIsSuccess() {
            return isSuccess;
        }
    }
}
