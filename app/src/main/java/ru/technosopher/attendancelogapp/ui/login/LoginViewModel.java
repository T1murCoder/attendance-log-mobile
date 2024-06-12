package ru.technosopher.attendancelogapp.ui.login;

import android.text.BoringLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.domain.sign.IsTeacherExistsUseCase;
import ru.technosopher.attendancelogapp.domain.sign.LoginTeacherUseCase;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<State> mutableTeacherLiveData = new MutableLiveData<>();
    public final LiveData<State> teacherLiveData = mutableTeacherLiveData;

    private final MutableLiveData<Boolean> mutableLoadingLiveData = new MutableLiveData<>();
    public final LiveData<Boolean> loadingLiveData = mutableLoadingLiveData;

    @Nullable
    private String login = null;
    @Nullable
    private String password = null;


    /*  USE CASES  */
    IsTeacherExistsUseCase isTeacherExistsUseCase = new IsTeacherExistsUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    LoginTeacherUseCase loginTeacherUseCase = new LoginTeacherUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    /*  USE CASES  */


    public void changeLogin(@NonNull String login) {
        this.login = login;
    }

    public void changePassword(@NonNull String password) {
        this.password = password;
    }

    public void confirm() {
        CredentialsDataSource.getInstance().logout();
        final String currentLogin = login;
        final String currentPassword = password;

        if ((currentLogin == null || currentLogin.isEmpty()) && (currentPassword == null || currentPassword.isEmpty())) {
            mutableErrorLiveData.postValue("Введите пароль");
            return;
        }
        if (currentLogin == null || currentLogin.isEmpty()) {
            mutableErrorLiveData.postValue("Введите логин");
            return;
        }
        if (currentPassword == null || currentPassword.isEmpty()) {
            mutableErrorLiveData.postValue("Введите пароль");
            return;
        }
        mutableLoadingLiveData.postValue(true);
        isTeacherExistsUseCase.execute(currentLogin, status -> {
            if (status.getErrors() != null || status.getValue() == null) {
                mutableLoadingLiveData.postValue(false);
                mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте позже");

                return;
            }
            if (status.getStatusCode() == 404) {
                mutableLoadingLiveData.postValue(false);
                mutableErrorLiveData.postValue("Этого логина не существует. Хотите создать аккаунт?");
                return;
            }
            if (status.getStatusCode() == 200) {
                loginTeacher(currentLogin, currentPassword);
            }
        });
    }

    private void loginTeacher(@NonNull final String currentLogin, @NonNull final String currentPassword) {
        loginTeacherUseCase.execute(currentLogin, currentPassword, status -> {
            if (status.getErrors() == null && status.getStatusCode() == 200) {

                login = currentLogin;
                password = currentPassword;



                mutableTeacherLiveData.postValue(new State(new TeacherEntity(
                        status.getValue().getId(),
                        status.getValue().getName(),
                        status.getValue().getSurname(),
                        status.getValue().getUsername(),
                        status.getValue().getTelegram_url(),
                        status.getValue().getGithub_url(),
                        status.getValue().getPhoto_url()

                ), password));

                mutableConfirmLiveData.postValue(null);
            }
            if (status.getErrors() == null && status.getStatusCode() == 401) {
                mutableErrorLiveData.postValue("Неверный пароль");
            }
            mutableLoadingLiveData.postValue(false);
        });
    }

    public class State {

        @Nullable
        private final TeacherEntity teacher;
        @Nullable
        private final String password;

        public State(@Nullable TeacherEntity teacher, @Nullable String password) {
            this.teacher = teacher;
            this.password = password;
        }
        @Nullable
        public String getPassword() {
            return password;
        }
        @Nullable
        public TeacherEntity getTeacher() {
            return teacher;
        }
    }
}
