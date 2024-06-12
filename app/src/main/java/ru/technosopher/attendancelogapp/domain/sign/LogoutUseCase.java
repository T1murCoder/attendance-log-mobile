package ru.technosopher.attendancelogapp.domain.sign;

public class LogoutUseCase {
    private final SignTeacherRepository repository;

    public LogoutUseCase(SignTeacherRepository repository) {
        this.repository = repository;
    }

    public void execute(){
        repository.logout();
    }
}
