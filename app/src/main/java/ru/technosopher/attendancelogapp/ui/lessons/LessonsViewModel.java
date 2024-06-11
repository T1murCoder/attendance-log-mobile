package ru.technosopher.attendancelogapp.ui.lessons;

import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import ru.technosopher.attendancelogapp.data.GroupsRepositoryImpl;
import ru.technosopher.attendancelogapp.data.LessonRepositoryImpl;
import ru.technosopher.attendancelogapp.data.QrCodeRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.groups.GetGroupNameByIdUseCase;
import ru.technosopher.attendancelogapp.domain.groups.GetGroupsListUseCase;
import ru.technosopher.attendancelogapp.domain.lessons.CreateLessonUseCase;
import ru.technosopher.attendancelogapp.domain.lessons.DeleteLessonUseCase;
import ru.technosopher.attendancelogapp.domain.lessons.GetLessonsListUseCase;
import ru.technosopher.attendancelogapp.domain.qrcode.CheckQrCodeIsAliveUseCase;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;

public class LessonsViewModel extends ViewModel {
    private MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();

    public LiveData<State> stateLiveData = mutableStateLiveData;
    private MutableLiveData<Boolean> mutableDeleteLiveData = new MutableLiveData<>();
    public LiveData<Boolean> deleteLiveData = mutableDeleteLiveData;
    private MutableLiveData<String> mutableAddErrorLiveData = new MutableLiveData<>();
    public LiveData<String> addErrorLiveData = mutableAddErrorLiveData;
    private MutableLiveData<GroupsState> mutableGroupsLiveData = new MutableLiveData<>();
    public LiveData<GroupsState> groupsLiveData = mutableGroupsLiveData;
    private MutableLiveData<QrCodeEntity> mutableItemQrCodeLiveData = new MutableLiveData<>();
    public LiveData<QrCodeEntity> itemQrCodeLiveData = mutableItemQrCodeLiveData;

    private final GetLessonsListUseCase getLessonsListUseCase = new GetLessonsListUseCase(
            LessonRepositoryImpl.getINSTANCE()
    );

    private final CreateLessonUseCase createLessonUseCase = new CreateLessonUseCase(
            LessonRepositoryImpl.getINSTANCE()
    );

    private final DeleteLessonUseCase deleteLessonUseCase = new DeleteLessonUseCase(
            LessonRepositoryImpl.getINSTANCE()
    );

    private final GetGroupsListUseCase getGroupsListUseCase = new GetGroupsListUseCase(
            GroupsRepositoryImpl.getInstance()
    );

    private final CheckQrCodeIsAliveUseCase checkQrCodeIsAliveUseCase = new CheckQrCodeIsAliveUseCase(
            QrCodeRepositoryImpl.getInstance()
    );
    /* USE CASES */
    private List<LessonEntity> lessons = new ArrayList<>();

    @Nullable
    private String groupId = null;
    @Nullable
    private String theme = null;
    @Nullable
    private GregorianCalendar timeStart = null;
    @Nullable
    private GregorianCalendar timeEnd = null;
    @Nullable
    private GregorianCalendar date = null;


    /* LOGIC */

    public LessonsViewModel() {
        update();
    }

    public void update() {
        getGroupsList();
        getLessonsList();
    }

    public void getLessonsList() {
        mutableStateLiveData.postValue(new State(null, null, false, true));
        getLessonsListUseCase.execute(
                status -> {
                    mutableStateLiveData.postValue(fromLessonStatus(status));
                }
        );
    }

    private State fromLessonStatus(Status<List<LessonEntity>> status) {
        if (status.getErrors() == null && status.getValue() != null) {
            lessons = status.getValue();
        }
        return new State(
                status.getValue() != null ? status.getValue() : null,
                status.getErrors() == null ? null : status.getErrors().getLocalizedMessage(),
                status.getErrors() == null && status.getValue() != null,
                false
        );
    }

    public void createLesson() {
        if (groupId == null) {
            mutableAddErrorLiveData.postValue("Выберете группу");
            return;
        }
        if (timeStart == null || timeEnd == null) {
            mutableAddErrorLiveData.postValue("Выберете время занятия");
            return;
        }
        if (date == null) {
            mutableAddErrorLiveData.postValue("Выберете время");
            return;
        }
        if (theme == null || theme.isEmpty()) {
            mutableAddErrorLiveData.postValue("Введите тему занятия");
            return;
        }

        createLessonUseCase.execute(theme, groupId, "", timeStart, timeEnd, date, lessonStatus -> {
            if (lessonStatus.getStatusCode() == 200) {
                if (lessonStatus.getValue() != null && lessonStatus.getErrors() == null) {
                    addNewLesson(lessonStatus.getValue());
                    update();
                } else {
                    mutableAddErrorLiveData.postValue("Что-то пошло не так");
                    clearAllFields();
                }
            }
        });
    }

    public void getGroupsList() {
        mutableGroupsLiveData.postValue(new GroupsState(null, null, false));
        getGroupsListUseCase.execute(status -> {
            mutableGroupsLiveData.postValue(fromGroupsStatus(status));
        });
    }

    private GroupsState fromGroupsStatus(Status<List<ItemGroupEntity>> status) {
        System.out.println(status.getStatusCode());
        return new GroupsState(
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getValue(),
                status.getErrors() == null && status.getValue() != null);
    }

    public void deleteLesson(@NonNull String id) {
        deleteLessonUseCase.execute(id, status -> {
            mutableDeleteLiveData.postValue(status.getStatusCode() == 200);
        });
    }


    public void checkQRCodeIsAlive(@NonNull String lessonId){
        checkQrCodeIsAliveUseCase.execute(lessonId, qrCodeEntityStatus ->{
            if (qrCodeEntityStatus.getStatusCode() == 200){
                mutableItemQrCodeLiveData.postValue(qrCodeEntityStatus.getValue());
            }
            else{
                System.out.println(qrCodeEntityStatus.getStatusCode());
            }
//            if (qrCodeEntityStatus.getStatusCode() == 409){
//                //error live data
//            }
        });
    }
    private void addNewLesson(LessonEntity lesson) {
        lessons.add(lesson);
    }

    public void changeTheme(Editable editable) {
        theme = String.valueOf(editable);
    }

    public void changeStartTime(Integer hour, Integer minute) {
        if (timeStart == null) timeStart = new GregorianCalendar();
        timeStart.set(Calendar.HOUR_OF_DAY, hour);
        timeStart.set(Calendar.MINUTE, minute);
    }

    public void changeEndTime(Integer hour, Integer minute) {
        if (timeEnd == null) timeEnd = new GregorianCalendar();
        timeEnd.set(Calendar.HOUR_OF_DAY, hour);
        timeEnd.set(Calendar.MINUTE, minute);

        if (timeStart == null) {
            mutableAddErrorLiveData.postValue("Время начала не заполнено");
            return;
        }
        if (timeEnd.compareTo(timeStart) <= 0)
            mutableAddErrorLiveData.postValue("Некорректное время конца занятия");
    }

    public void changeDate(int year, int month, int day) {
        if (date == null) date = new GregorianCalendar();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
        changeStartAndEndDate(year, month, day);
    }

    public String getFullTime() {
        if (timeEnd != null && timeStart != null) {
            return DateFormatter.getFullTimeStringFromDate(timeStart, timeEnd, "HH:mm");
        } else {
            mutableAddErrorLiveData.postValue("Некорректное время");
            return "";
        }
    }

    public String getDate() {
        if (date != null) return DateFormatter.getDateStringFromDate(date, "dd MMM yyyy");
        else {
            mutableAddErrorLiveData.postValue("Некорректная дата");
            return "";
        }
    }

    public void changeStartAndEndDate(int year, int month, int day) {
        if (timeStart == null) timeStart = new GregorianCalendar();
        timeStart.set(Calendar.YEAR, year);
        timeStart.set(Calendar.MONTH, month);
        timeStart.set(Calendar.DAY_OF_MONTH, day);

        if (timeEnd == null) timeEnd = new GregorianCalendar();
        timeEnd.set(Calendar.YEAR, year);
        timeEnd.set(Calendar.MONTH, month);
        timeEnd.set(Calendar.DAY_OF_MONTH, day);
    }

    public void changeGroup(String groupId) {
        if (Objects.equals(groupId, "-1")) return;
        this.groupId = groupId;
    }

    public void clearAllFields() {
        date = null;
        timeStart = null;
        timeEnd = null;
        theme = null;
        groupId = null;
    }

    /* LOGIC */
    public class State {
        @Nullable
        private final List<LessonEntity> lessons;
        @Nullable
        private final String errorMessage;
        @NonNull
        private Boolean isSuccess;
        @NonNull
        private Boolean isLoading;

        public State(@Nullable List<LessonEntity> lessons, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.lessons = lessons;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<LessonEntity> getLessons() {
            return lessons;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @NonNull
        public Boolean getSuccess() {
            return isSuccess;
        }

        @NonNull
        public Boolean getLoading() {
            return isLoading;
        }
    }

    public class GroupsState {
        @Nullable
        private final List<ItemGroupEntity> groups;
        @Nullable
        private final String errorMessage;
        @NonNull
        private final Boolean isSuccess;

        public GroupsState(@Nullable String errorMessage, @Nullable List<ItemGroupEntity> groups, @NonNull Boolean isSuccess) {
            this.groups = groups;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
        }

        @Nullable
        public List<ItemGroupEntity> getGroups() {
            return groups;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @NonNull
        public Boolean getSuccess() {
            return isSuccess;
        }

    }
}
