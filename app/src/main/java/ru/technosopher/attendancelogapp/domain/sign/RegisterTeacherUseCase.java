package ru.technosopher.attendancelogapp.domain.sign;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.domain.TeacherRepository;
import ru.technosopher.attendancelogapp.domain.entities.Status;

public class RegisterTeacherUseCase {
    private final SignTeacherRepository repository;

    public RegisterTeacherUseCase(SignTeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(@NonNull String login, @NonNull String password, @NonNull String name, @NonNull String surname,Consumer<Status<Void>> callback) {
        repository.registerTeacher(login, password, name, surname, callback);
    }
}
