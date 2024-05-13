package ru.technosopher.attendancelogapp.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import okhttp3.Call;
import ru.technosopher.attendancelogapp.data.dto.TeacherAccountDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogapp.data.source.TeacherApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.domain.TeacherRepository;
import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.domain.sign.SignTeacherRepository;

public class TeacherRepositoryImpl implements TeacherRepository, SignTeacherRepository {


    private static TeacherRepositoryImpl INSTANCE;

    private TeacherApi teacherApi = RetrofitFactory.getInstance().getTeacherApi();

    private CredentialsDataSource credentialsDataSource = CredentialsDataSource.getInstance();

    private TeacherRepositoryImpl() {
    }

    public static synchronized TeacherRepositoryImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TeacherRepositoryImpl();
        }
        return INSTANCE;
    }


    @Override
    public void getAllTechers(@NonNull Consumer<Status<List<ItemTeacherEntity>>> callback) {

    }

    @Override
    public void getTeacher(@NonNull String id, Consumer<Status<TeacherEntity>> callback) {

    }

    @Override
    public void isExists(@NonNull String login, Consumer<Status<Void>> callback) {
        teacherApi.isExists(login).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void registerTeacher(@NonNull String login, @NonNull String password, @NonNull String name, @NonNull String surname, Consumer<Status<Void>> callback) {
        teacherApi.register(new TeacherAccountDto(login, password, name, surname)).enqueue(new CallToConsumer<>(
                callback,
                dto -> null

        ));
    }

    @Override
    public void loginTeacher(@NonNull String login, @NonNull String password, Consumer<Status<Void>> callback) {
        credentialsDataSource.updateLogin(login, password);
        teacherApi = RetrofitFactory.getInstance().getTeacherApi();
        // TODO( DO smth with login data )
        teacherApi.login().enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));

    }

    @Override
    public void logout() {
        credentialsDataSource.logout();
    }
}
