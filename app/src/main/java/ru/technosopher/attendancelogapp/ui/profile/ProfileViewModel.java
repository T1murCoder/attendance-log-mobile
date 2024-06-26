package ru.technosopher.attendancelogapp.ui.profile;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ru.technosopher.attendancelogapp.data.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.teacher.GetTeacherByIdUseCase;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.domain.sign.LogoutUseCase;
import ru.technosopher.attendancelogapp.domain.teacher.UpdateTeacherProfileUseCase;

public class ProfileViewModel extends ViewModel {

    public static final String TAG = "PROFILE_VIEW_MODEL";
    public static final String AVATAR_PREFIX = "images/avatar_";
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();

    public final LiveData<State> stateLiveData = mutableStateLiveData;
    private final MutableLiveData<Void> mutableLogoutLiveData = new MutableLiveData<>();
    public final LiveData<Void> logoutLiveData = mutableLogoutLiveData;
    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    private final UpdateTeacherProfileUseCase updateTeacherProfileUseCase = new UpdateTeacherProfileUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    private final GetTeacherByIdUseCase getTeacherByIdUseCase = new GetTeacherByIdUseCase(
            TeacherRepositoryImpl.getInstance()
    );
    @Nullable
    private String name;
    @Nullable
    private String surname;
    @Nullable
    private String telegram;
    @Nullable
    private String github;
    @Nullable
    private String photo;

    public void loadPrefs(String id, String prefsLogin, String prefsName, String prefsSurname, String prefsTelegram, String prefsGithub, String prefsPhotoUrl) {
        getTeacherByIdUseCase.execute(id, teacher -> {
            if (teacher == null) {
                changeName(prefsName);
                changeSurname(prefsSurname);
                changeTelegram(prefsTelegram);
                changeGithub(prefsGithub);
                changePhoto(prefsPhotoUrl);
                mutableStateLiveData.postValue(new State(null, new TeacherEntity(
                        id,
                        prefsName,
                        prefsSurname,
                        prefsLogin,
                        prefsTelegram,
                        prefsGithub,
                        prefsPhotoUrl
                ), false));

            } else {
                changeName(teacher.getValue().getName());
                changeSurname(teacher.getValue().getSurname());
                changeTelegram(teacher.getValue().getTelegram_url());
                changeGithub(teacher.getValue().getGithub_url());
                changePhoto(teacher.getValue().getPhoto_url());

                //Log.e("ProfileViewModel", teacher.getValue().getName());
                //Log.e("ProfileViewModel", teacher.getValue().getSurname());

                mutableStateLiveData.postValue(new State(
                        null, new TeacherEntity(
                        id,
                        teacher.getValue().getName(),
                        teacher.getValue().getSurname(),
                        teacher.getValue().getUsername(),
                        teacher.getValue().getTelegram_url(),
                        teacher.getValue().getGithub_url(),
                        teacher.getValue().getPhoto_url()
                ), false
                ));

            }
        });

    }

    public void updateProfile(String id, String prefsLogin) {
        if (name == null || surname == null || name.isEmpty() || surname.isEmpty()) {
            mutableStateLiveData.postValue(new State("Имя и фамилия не могут быть пустыми", null, false));
        } else {
            mutableStateLiveData.postValue(new State(null, null, true));
            updateTeacherProfileUseCase.execute(
                    id,
                    new TeacherEntity(
                            id,
                            name,
                            surname,
                            prefsLogin,
                            telegram,
                            github,
                            photo),
                    tstatus -> {
                        if (tstatus.getStatusCode() == 200) {
                            loadPrefs(id, prefsLogin, name, surname, telegram, github, photo);
                        } else {
                            Log.e("ProfileViewModel", "" + tstatus.getStatusCode());
                            mutableStateLiveData.postValue(new State("Что то пошло не так. Попробуйте еще раз", null, false));
                        }
                    });
        }
    }

    public void uploadAvatar(String id, String prefsLogin, Uri image) {
        if (image != null) {
            //TODO: Сделать сжатие изображения
            StorageReference imageRef = storageRef.child(AVATAR_PREFIX + id + ".png");

            imageRef.putFile(image).addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG, "Image loaded!");
                updateTeacherProfileUseCase.execute(
                        id,
                        new TeacherEntity(
                                id,
                                null,
                                null,
                                null,
                                null,
                                null,
                                imageRef.getPath()
                        ),
                        userStatus -> loadPrefs(id, prefsLogin, name, surname, telegram, github, imageRef.getPath())
                );
            }).addOnFailureListener(e -> {
                Log.d(TAG, e.toString());
                mutableStateLiveData.postValue(new State("Не получилось загрузить аватар!", null, false));
            });
        } else {
            Log.d(TAG, "Image is null!");
        }
    }

    public void logout() {
        logoutUseCase.execute();
        mutableLogoutLiveData.postValue(null);
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeSurname(String surname) {
        this.surname = surname;
    }

    public void changeTelegram(String telegram) {
        this.telegram = telegram;
    }

    public void changeGithub(String github) {
        this.github = github;
    }

    public void changePhoto(String photo) {
        this.photo = photo;
    }
    //TODO(PHOTO LOADING)

    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final TeacherEntity teacher;

        @Nullable
        private final Boolean loading;

        public State(@Nullable String errorMessage, @Nullable TeacherEntity teacher, @Nullable Boolean loading) {
            this.errorMessage = errorMessage;
            this.teacher = teacher;
            this.loading = loading;
        }

        @Nullable
        public TeacherEntity getTeacher() {
            return teacher;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public Boolean getLoading() {
            return loading;
        }
    }
}
