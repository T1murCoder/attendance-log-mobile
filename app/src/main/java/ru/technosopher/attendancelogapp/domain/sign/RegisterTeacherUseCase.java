package ru.technosopher.attendancelogapp.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherAccountEntity;

public class RegisterTeacherUseCase {
    private final SignTeacherRepository repository;

    public RegisterTeacherUseCase(SignTeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String login, @NonNull String password, @NonNull String name, @NonNull String surname,Consumer<Status<TeacherAccountEntity>> callback) {
        repository.registerTeacher(login, password, name, surname, callback);
    }
}
