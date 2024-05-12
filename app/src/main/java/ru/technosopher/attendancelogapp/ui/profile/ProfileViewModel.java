package ru.technosopher.attendancelogapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.GetTeacherByIdUseCase;
import ru.technosopher.attendancelogapp.domain.TeacherRepository;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    public final GetTeacherByIdUseCase getTeacherByIdUseCase = new GetTeacherByIdUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    public void load(@NonNull String id){
        mutableStateLiveData.setValue(new State(null, null, true));
        getTeacherByIdUseCase.execute(id, status->{
            mutableStateLiveData.postValue(new State(
                    status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                    status.getValue(),
                    false
            ));
        });
    }

    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final TeacherEntity teacher;
        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable TeacherEntity teacher, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.teacher = teacher;
            this.isLoading = isLoading;
        }

        @Nullable
        public TeacherEntity getTeacher() {
            return teacher;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean isLoading(){
            return isLoading;
        }
    }
}
