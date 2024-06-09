package ru.technosopher.attendancelogapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.teacher.GetTeacherByIdUseCase;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.domain.sign.LogoutUseCase;
import ru.technosopher.attendancelogapp.domain.teacher.UpdateTeacherProfileUseCase;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();

    public final LiveData<State> stateLiveData = mutableStateLiveData;
    private final MutableLiveData<Void> mutableLogoutLiveData = new MutableLiveData<>();
    public final LiveData<Void> logoutLiveData = mutableLogoutLiveData;
    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    private final UpdateTeacherProfileUseCase updateTeacherProfileUseCase = new UpdateTeacherProfileUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    @Nullable
    private String name;

    @Nullable
    private String surname;

    @Nullable
    private String telegram;

    @Nullable
    private String github;

    @Nullable
    private String photo;

    public void loadPrefs(String id, String prefsLogin, String prefsName, String prefsSurname, String prefsTelegram, String prefsGithub, String prefsPhotoUrl) {
        mutableStateLiveData.postValue(new State(null, new TeacherEntity(
                id,
                prefsName,
                prefsSurname,
                prefsLogin,
                prefsTelegram,
                prefsGithub,
                prefsPhotoUrl
        ), false));
        changeName(prefsName);
        changeSurname(prefsSurname);
        changeTelegram(prefsTelegram);
        changeGithub(prefsGithub);
    }

    public void updateProfile(String id, String prefsLogin) {
        if (name == null || surname == null || name.isEmpty() || surname.isEmpty()) {
            mutableStateLiveData.postValue(new State("Имя и фамилия не могут быть пустыми", null, false));
        } else {
            //TODO(fix untouched fields update)
            mutableStateLiveData.postValue(new State(null, null, true));
            updateTeacherProfileUseCase.execute(
                    id,
                    new TeacherEntity(
                            id,
                            name,
                            surname,
                            prefsLogin,
                            telegram,
                            github,
                            photo),
                    tstatus -> {
                        System.out.println(tstatus.getStatusCode());
                        System.out.println(tstatus.getValue());
                        if (tstatus.getStatusCode() == 200) {
                            loadPrefs(id, prefsLogin, name, surname, telegram, prefsLogin, photo);
                        } else {
                            mutableStateLiveData.postValue(new State("Что то пошло не так. Попробуйте еще раз", null, false));
                        }


                    });
        }
    }

    public void logout() {
        logoutUseCase.execute();
        mutableLogoutLiveData.postValue(null);
    }

    public void changeName(String name){
        this.name = name;
    }
    public void changeSurname(String surname){
        this.surname = surname;
    }
    public void changeTelegram(String telegram){
        this.telegram = telegram;
    }
    public void changeGithub(String github){
        this.github = github;
    }

    //TODO(PHOTO LOADING)

    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final TeacherEntity teacher;

        @Nullable
        private final Boolean loading;
        public State(@Nullable String errorMessage, @Nullable TeacherEntity teacher, @Nullable Boolean loading) {
            this.errorMessage = errorMessage;
            this.teacher = teacher;
            this.loading = loading;
        }

        @Nullable
        public TeacherEntity getTeacher() {
            return teacher;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public Boolean getLoading() {
            return loading;
        }
    }
}
