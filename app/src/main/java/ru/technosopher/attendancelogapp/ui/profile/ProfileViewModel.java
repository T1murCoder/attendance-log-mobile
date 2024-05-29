package ru.technosopher.attendancelogapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.GetTeacherByIdUseCase;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;
    public final GetTeacherByIdUseCase getTeacherByIdUseCase = new GetTeacherByIdUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    public void load(@NonNull String id) {
        getTeacherByIdUseCase.execute(id, status -> {
            mutableStateLiveData.postValue(new State(
                    status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                    status.getValue()
            ));
        });
    }

    public void updatePrefs() {
        //TODO(SUFFER.............)
    }

    public void loadPrefs(String id, String prefsLogin, String prefsName, String prefsSurname, String prefsTelegram, String prefsGithub, String prefsPhotoUrl) {
        System.out.println(prefsGithub);
        System.out.println(prefsTelegram);
        // TODO(VALIDATION)
        mutableStateLiveData.postValue(new State(null, new TeacherEntity(
                id,
                prefsName,
                prefsSurname,
                prefsLogin,
                prefsTelegram,
                prefsGithub,
                prefsPhotoUrl
        )));
    }


    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final TeacherEntity teacher;

        public State(@Nullable String errorMessage, @Nullable TeacherEntity teacher) {
            this.errorMessage = errorMessage;
            this.teacher = teacher;
        }

        @Nullable
        public TeacherEntity getTeacher() {
            return teacher;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

    }
}
