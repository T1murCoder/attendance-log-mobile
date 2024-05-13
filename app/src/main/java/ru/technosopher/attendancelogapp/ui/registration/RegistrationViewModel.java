package ru.technosopher.attendancelogapp.ui.registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.sign.IsTeacherExistsUseCase;
import ru.technosopher.attendancelogapp.domain.sign.RegisterTeacherUseCase;

public class RegistrationViewModel extends ViewModel {
    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;

    /* USE CASES */
    private IsTeacherExistsUseCase isTeacherExistsUseCase = new IsTeacherExistsUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    private RegisterTeacherUseCase registerTeacherUseCase = new RegisterTeacherUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    /* USE CASES */

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

    public void confirm(){
        final String currentName = name;
        final String currentSurname = surname;
        final String currentLogin = login;
        final String currentPassword = password;

        if (currentName == null || currentName.isEmpty() ||
                currentSurname == null || currentSurname.isEmpty() ||
                currentLogin == null || currentLogin.isEmpty() ||
                currentPassword == null || currentPassword.isEmpty()){
            mutableErrorLiveData.postValue("Provide all necessary data to fields, please.");
            return;
        }
        isTeacherExistsUseCase.execute(currentLogin, status -> {
            if (status.getErrors() != null || status.getValue() == null) {
                mutableErrorLiveData.postValue("Something went wrong. Try again later");
                return;
            }
            if (status.getStatusCode() == 200) {
                mutableErrorLiveData.postValue("This login is already exists. Want to login?");
                return;
            }
            if (status.getStatusCode() == 404) {
                registerTeacher(login, password, name, surname);
            }
        });
    }

    private void registerTeacher(@NonNull String currentLogin,@NonNull String currentPassword,@NonNull String currentName,@NonNull String currentSurname) {
        registerTeacherUseCase.execute(currentLogin, currentPassword, currentName, currentSurname, status->{
            if (status.getErrors() == null && status.getStatusCode() == 200) {
                login = currentLogin;
                password = currentPassword;
                name = currentName;
                surname = currentSurname;
                mutableConfirmLiveData.postValue(null);
            }
            if (status.getErrors() == null && status.getStatusCode() == 401) {
                mutableErrorLiveData.postValue("This account is already exists. Want to login?");
            } else {
                mutableErrorLiveData.postValue("Something went wrong. Try again later");
            }
        });
    }

}
