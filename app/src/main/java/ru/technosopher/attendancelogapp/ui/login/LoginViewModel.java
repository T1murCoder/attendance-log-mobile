package ru.technosopher.attendancelogapp.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
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
        final String currentLogin = login;
        final String currentPassword = password;

        if ((currentLogin == null || currentLogin.isEmpty()) && (currentPassword == null || currentPassword.isEmpty())) {
            mutableErrorLiveData.postValue("Please, enter login and password.");
            return;
        }
        if (currentLogin == null || currentLogin.isEmpty()) {
            mutableErrorLiveData.postValue("Please, enter login.");
            return;
        }
        if (currentPassword == null || currentPassword.isEmpty()) {
            mutableErrorLiveData.postValue("Please, enter password.");
            return;
        }
        isTeacherExistsUseCase.execute(currentLogin, status -> {
            if (status.getErrors() != null || status.getValue() == null) {
                System.out.println(status.getErrors().getLocalizedMessage());
                mutableErrorLiveData.postValue("Something went wrong with server. Try again later");
                return;
            }
            if (status.getStatusCode() == 404) {
                mutableErrorLiveData.postValue("This login doesn`t exist. Want to create account?");
                return;
            }
            if (status.getStatusCode() == 200) {
                loginTeacher(currentLogin, currentPassword);
            }
        });
    }

    public void testIsTeacherExists() {
        final String currentLogin = login;
        assert currentLogin != null;
        isTeacherExistsUseCase.execute(currentLogin, status -> {
            if (status.getErrors() != null || status.getValue() == null) {
//                System.out.println(status.getErrors().getLocalizedMessage());
//                System.out.println(status.getValue());
//                System.out.println(status.getStatusCode());
                mutableErrorLiveData.postValue("Something went wrong with requests. Try again later");
                return;
            }
            if (status.getStatusCode() == 404) {
                mutableErrorLiveData.postValue("This login doesn`t exist. Want to create account?");
                return;
            }
            if (status.getStatusCode() == 200) {
                System.out.println(status.getValue());
                System.out.println(status.getStatusCode());
                mutableErrorLiveData.postValue("This login exists. Its okay");
                //loginTeacher(currentLogin, currentPassword);
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
                mutableErrorLiveData.postValue("Wrong password");
            }
//            } else {
//                mutableErrorLiveData.postValue("Something went wrong. Try again later");
//            }
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
