package ru.technosopher.attendancelogapp.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public class LoginTeacherUseCase {

    private final SignTeacherRepository repository;

    public LoginTeacherUseCase(SignTeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String login, @NonNull String password, Consumer<Status<TeacherEntity>> callback) {
        repository.loginTeacher(login, password, status->{
            if (status.getStatusCode() != 200) repository.logout();
            callback.accept(status);
        });

    }
}
