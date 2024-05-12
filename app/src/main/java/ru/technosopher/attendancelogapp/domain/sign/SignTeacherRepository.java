package ru.technosopher.attendancelogapp.domain.sign;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import javax.security.auth.callback.Callback;

import ru.technosopher.attendancelogapp.domain.entities.Status;

import java.util.function.Consumer;

public interface SignTeacherRepository {

    void isExists(@NonNull String login, Consumer<Status<Void>> callback);

    void registerTeacher(@NonNull String login,
                         @NonNull String password,
                         @NonNull String name,
                         @NonNull String surname,
                         Consumer<Status<Void>> callback);

    void loginTeacher(@NonNull String login, @NonNull String password, Consumer<Status<Void>> callback);

    void logout();
}
