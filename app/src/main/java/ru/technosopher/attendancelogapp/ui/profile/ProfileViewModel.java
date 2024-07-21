package ru.technosopher.attendancelogapp.ui.profile;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileDescriptor;
import java.io.IOException;

import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.technosopher.attendancelogapp.data.repository.ImageRepositoryImpl;
import ru.technosopher.attendancelogapp.data.repository.TeacherRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.ImageEntity;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.domain.images.UploadProfileImageUseCase;
import ru.technosopher.attendancelogapp.domain.sign.LogoutUseCase;
import ru.technosopher.attendancelogapp.domain.teacher.GetTeacherByIdUseCase;
import ru.technosopher.attendancelogapp.domain.teacher.UpdateTeacherProfileUseCase;

public class ProfileViewModel extends ViewModel {

    public static final String TAG = "PROFILE_VIEW_MODEL";

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<Void> mutableLogoutLiveData = new MutableLiveData<>();
    public final LiveData<Void> logoutLiveData = mutableLogoutLiveData;

    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    private final GetTeacherByIdUseCase getUserByIdUseCase = new GetTeacherByIdUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    private final UpdateTeacherProfileUseCase updateTeacherProfileUseCase = new UpdateTeacherProfileUseCase(
            TeacherRepositoryImpl.getInstance()
    );

    private final UploadProfileImageUseCase uploadProfileImageUseCase = new UploadProfileImageUseCase(
            ImageRepositoryImpl.getINSTANCE()
    );

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    @Nullable
    private String id;
    @Nullable
    private String login;
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
        changeId(id);
        changeName(prefsName);
        changeSurname(prefsSurname);
        changeTelegram(prefsTelegram);
        changeGithub(prefsGithub);
        changePhoto(prefsPhotoUrl);
        changeLogin(prefsLogin);
    }

    public void update(String id, String prefsLogin, String prefsName, String prefsSurname, String prefsTelegram, String prefsGithub, String prefsPhotoUrl) {
        loadPrefs(id, prefsLogin, prefsName, prefsSurname, prefsTelegram, prefsGithub, prefsPhotoUrl);
        mutableStateLiveData.postValue(new State(null, new TeacherEntity(
                id,
                name,
                surname,
                login,
                telegram,
                github,
                photo
        ), false));
    }

    private void update(String photoUrl) {
        loadPrefs(id, login, name, surname, telegram, github, photoUrl);
        mutableStateLiveData.postValue(new State(null, new TeacherEntity(
                id,
                name,
                surname,
                login,
                telegram,
                github,
                photoUrl
        ), false));
    }

    @SuppressLint("CheckResult")
    public void uploadAvatar(String id, Uri image, ContentResolver contentResolver) {
        if (image != null) {
            mutableStateLiveData.postValue(new State("Загрузка...", null, false));
            try {

                Bitmap imageBitmap = getBitmapFromUri(image, contentResolver);
                if (imageBitmap != null) {
                    uploadProfileImageUseCase.execute(id, imageBitmap)
                            .subscribeOn(Schedulers.io())
                            .subscribe((photoUrl, throwable) -> {
                                if (throwable != null) {
                                    mutableStateLiveData.postValue(new State("Не удалось загрузить аватар!", null, false));
                                } else {
                                    Log.d(TAG, "Запрос сделан!");
                                    updateTeacherProfileUseCase.execute(
                                            id,
                                            new TeacherEntity(
                                                    id,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    photoUrl
                                            ),
                                            userStatus -> update(photoUrl));
                                }
                            });
                } else {
                    mutableStateLiveData.postValue(new State("Что-то пошло не так...", null, false));
                }
            } catch (IOException e) {
                mutableStateLiveData.postValue(new State("Что-то пошло не так...", null, false));
            }
        } else {
            mutableStateLiveData.postValue(new State("Пожалуйста, выберите изображение", null, false));
        }
    }

    private Bitmap getBitmapFromUri(Uri uri, ContentResolver contentResolver) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
        if (parcelFileDescriptor != null) {
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        }
        return null;
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
                    userstatus -> {
                        if (userstatus.getStatusCode() == 200) {
                            update(id, prefsLogin, name, surname, telegram, github, photo);
                        } else {
                            mutableStateLiveData.postValue(new State("Что то пошло не так. Попробуйте еще раз", null, false));
                        }
                    });
        }
    }

    public void changeName(String name){
        this.name = name.trim();
    }
    public void changeSurname(String surname){
        this.surname = surname.trim();
    }
    public void changeTelegram(String telegram){
        this.telegram = telegram;
    }
    public void changeGithub(String github){
        this.github = github;
    }
    public void changePhoto(String photo) {this.photo = photo;}
    public void changeLogin(String login) {this.login = login;}
    private void changeId(String id) {this.id = id;}

    public void logout() {
        logoutUseCase.execute();
        mutableLogoutLiveData.postValue(null);
    }

    public class State {
        @Nullable
        private final String errorMessage;
        @Nullable
        private final TeacherEntity user;

        @Nullable
        private final Boolean loading;

        public State(@Nullable String errorMessage, @Nullable TeacherEntity user, @Nullable Boolean loading) {
            this.errorMessage = errorMessage;
            this.user = user;
            this.loading = loading;
        }

        @Nullable
        public TeacherEntity getUser() {
            return user;
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