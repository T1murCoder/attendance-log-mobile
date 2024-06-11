package ru.technosopher.attendancelogapp.ui.registration;

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
import ru.technosopher.attendancelogapp.domain.sign.RegisterTeacherUseCase;

public class RegistrationViewModel extends ViewModel {
    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<State> mutableTeacherLiveData = new MutableLiveData<>();
    public final LiveData<State> teacherLiveData = mutableTeacherLiveData;

    private final MutableLiveData<Boolean> mutableLoadingLiveData = new MutableLiveData<>();
    public final LiveData<Boolean> loadingLiveData = mutableLoadingLiveData;

    /* USE CASES */
    private IsTeacherExistsUseCase isTeacherExistsUseCase = new IsTeacherExistsUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    private RegisterTeacherUseCase registerTeacherUseCase = new RegisterTeacherUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    private LoginTeacherUseCase loginTeacherUseCase = new LoginTeacherUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    /* USE CASES */


    @Nullable
    private String id = null;
    @Nullable
    private String name = null;

    @Nullable
    private String surname = null;

    @Nullable
    private String login = null;

    @Nullable
    private String password = null;


    public void changeLogin(String login) {
        this.login = login;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeSurname(String surname) {
        this.surname = surname;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void confirm() {
        CredentialsDataSource.getInstance().logout();
        final String currentName = name;
        final String currentSurname = surname;
        final String currentLogin = login;
        final String currentPassword = password;

        if (currentName == null || currentName.isEmpty() ||
                currentSurname == null || currentSurname.isEmpty() ||
                currentLogin == null || currentLogin.isEmpty() ||
                currentPassword == null || currentPassword.isEmpty()) {
            mutableErrorLiveData.postValue("Provide all necessary data to fields, please.");
            return;
        }
        mutableLoadingLiveData.postValue(true);
        isTeacherExistsUseCase.execute(currentLogin, status -> {
            if (status.getErrors() != null || status.getValue() == null) {
                System.out.println(status.getErrors().getLocalizedMessage());
                mutableLoadingLiveData.postValue(false);
                mutableErrorLiveData.postValue("Something went wrong with server. Try again later");
                return;
            }
            if (status.getStatusCode() == 200) {
                mutableLoadingLiveData.postValue(false);
                mutableErrorLiveData.postValue("This login is already exists. Want to login?");
                return;
            }
            if (status.getStatusCode() == 404) {
                registerTeacher(login, password, name, surname);
            }
        });
    }

    private void registerTeacher(@NonNull String currentLogin, @NonNull String currentPassword, @NonNull String currentName, @NonNull String currentSurname) {
        registerTeacherUseCase.execute(currentLogin, currentPassword, currentName, currentSurname, status -> {
            if (status.getErrors() == null && status.getStatusCode() == 200) {
                id = status.getValue().getId();
                login = currentLogin;
                password = currentPassword;
                name = currentName;
                surname = currentSurname;

                mutableTeacherLiveData.postValue(new State(new TeacherEntity(
                        status.getValue().getId(),
                        status.getValue().getName(),
                        status.getValue().getSurname(),
                        status.getValue().getUsername(),
                        null,
                        null,
                        null

                ), password));
                mutableConfirmLiveData.postValue(null);
            }
            else if (status.getErrors() == null && status.getStatusCode() == 401) {
                mutableErrorLiveData.postValue("This account is already exists. Want to login??????");
            }
            mutableLoadingLiveData.postValue(false);
//            } else {
//                //System.out.println(status.getStatusCode());
//                mutableErrorLiveData.postValue("Something went wrong before reg. Try again later");
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
