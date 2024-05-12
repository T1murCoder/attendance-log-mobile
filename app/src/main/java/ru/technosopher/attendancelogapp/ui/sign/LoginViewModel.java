package ru.technosopher.attendancelogapp.ui.sign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.domain.sign.IsTeacherExistsUseCase;
import ru.technosopher.attendancelogapp.domain.sign.LoginTeacherUseCase;
import ru.technosopher.attendancelogapp.domain.sign.RegisterTeacherUseCase;
import ru.technosopher.attendancelogapp.domain.sign.SignTeacherRepository;
import ru.technosopher.attendancelogapp.ui.profile.ProfileViewModel;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;

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
                mutableErrorLiveData.postValue("Something went wrong. Try again later");
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

    private void loginTeacher(@NonNull final String currentLogin, @NonNull final String currentPassword) {
        loginTeacherUseCase.execute(currentLogin, currentPassword, status -> {
            if (status.getErrors() == null && status.getStatusCode() == 200) {
                //TODO(something better with auth data)
                login = currentLogin;
                password = currentPassword;

                mutableConfirmLiveData.postValue(null);
            }
            if (status.getErrors() == null && status.getStatusCode() == 401) {
                mutableErrorLiveData.postValue("Wrong password");
            } else {
                mutableErrorLiveData.postValue("Something went wrong. Try again later");
            }
        });
    }

//    public class State {
//        @Nullable
//        private final String login;
//        @Nullable
//        private final String password;
//
//        @Nullable
//        public String getLogin() {
//            return login;
//        }
//
//        @Nullable
//        public String getPassword() {
//            return password;
//        }
//
//        public State(@Nullable String login, @Nullable String password) {
//            this.login = login;
//            this.password = password;
//        }
//    }
}
