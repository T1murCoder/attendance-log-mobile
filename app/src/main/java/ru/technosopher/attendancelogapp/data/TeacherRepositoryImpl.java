package ru.technosopher.attendancelogapp.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.data.dto.TeacherRegisterDto;
import ru.technosopher.attendancelogapp.data.network.RetrofitFactory;
import ru.technosopher.attendancelogapp.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogapp.data.source.TeacherApi;
import ru.technosopher.attendancelogapp.data.utils.CallToConsumer;
import ru.technosopher.attendancelogapp.data.utils.Mapper;
import ru.technosopher.attendancelogapp.domain.teacher.TeacherRepository;
import ru.technosopher.attendancelogapp.domain.entities.ItemTeacherEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.entities.TeacherAccountEntity;
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
        teacherApi = RetrofitFactory.getInstance().getTeacherApi();
        teacherApi.login().enqueue(new CallToConsumer<>(
                callback,
                teacher -> {
                    if (teacher != null){
                        if (teacher.id != null && teacher.name != null && teacher.surname != null && teacher.username != null){
                            final String name = teacher.name;
                            final String surname = teacher.surname;
                            final String username = teacher.username;
                            return new TeacherEntity(
                                    id,
                                    name,
                                    surname,
                                    username,
                                    teacher.telegram_url,
                                    teacher.github_url,
                                    teacher.photo_url);
                        }
                    }
                    return null;
                }
        ));
    }

    @Override
    public void updateProfile(@NonNull String id, @NonNull TeacherEntity updatedTeacher, Consumer<Status<TeacherEntity>> callback) {
        teacherApi.update(id, Mapper.fromTeacherEntityToDto(updatedTeacher)).enqueue(new CallToConsumer<>(
                callback,
                Mapper::fromTeacherDtoToEntity
        ));
    }

    @Override
    public void isExists(@NonNull String login, Consumer<Status<Void>> callback) {
        teacherApi.isExists(login).enqueue(new CallToConsumer<>(
                callback,
                dto -> null
        ));
    }

    @Override
    public void registerTeacher(@NonNull String login,
                                @NonNull String password,
                                @NonNull String name,
                                @NonNull String surname, Consumer<Status<TeacherAccountEntity>> callback) {
        teacherApi.register(new TeacherRegisterDto(login, password, name, surname)).enqueue(new CallToConsumer<>(
                callback,
                teacherAcc -> {
                    if (teacherAcc != null){
                        credentialsDataSource.updateLogin(login, password);
                        return new TeacherAccountEntity(
                                teacherAcc.getId(),
                                teacherAcc.getName(),
                                teacherAcc.getSurname(),
                                teacherAcc.getUsername(),
                                password);
                    }
                    return null;
                }
        ));

    }

    @Override
    public void loginTeacher(@NonNull String login, @NonNull String password, Consumer<Status<TeacherEntity>> callback) {
        credentialsDataSource.updateLogin(login, password);
        teacherApi = RetrofitFactory.getInstance().getTeacherApi();
        teacherApi.login().enqueue(new CallToConsumer<>(
                callback,
                teacher -> {
                    if (teacher != null){
                        if (teacher.id != null && teacher.name != null && teacher.surname != null && teacher.username != null){
                            final String id = teacher.id;
                            final String name = teacher.name;
                            final String surname = teacher.surname;
                            final String username = teacher.username;
                            System.out.println(teacher.telegram_url);
                            System.out.println(teacher.github_url);
                            return new TeacherEntity(
                                    id,
                                    name,
                                    surname,
                                    username,
                                    teacher.telegram_url,
                                    teacher.github_url,
                                    teacher.photo_url);
                        }
                    }
                    return null;
                }
        ));
    }
    @Override
    public void logout() {
        credentialsDataSource.logout();
    }
}
