package ru.technosopher.attendancelogapp.ui.table;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.technosopher.attendancelogapp.data.repository.AttendanceRepositoryImpl;
import ru.technosopher.attendancelogapp.data.repository.GroupsRepositoryImpl;
import ru.technosopher.attendancelogapp.data.repository.StudentRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.attendance.ChangeStudentAttAndPointsUseCase;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;
import ru.technosopher.attendancelogapp.domain.groups.DeleteStudentFromGroupUseCase;
import ru.technosopher.attendancelogapp.domain.groups.GetGroupNameByIdUseCase;
import ru.technosopher.attendancelogapp.domain.students.GetStudentsAttendancesUseCase;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;

public class TableViewModel extends ViewModel {
    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public LiveData<State> stateLiveData = mutableStateLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<Boolean> mutableDeleteLiveData = new MutableLiveData<>();
    public LiveData<Boolean> deleteLiveData = mutableDeleteLiveData;

    /* USE CASES */
    private final GetStudentsAttendancesUseCase getStudentsAttendancesUseCase = new GetStudentsAttendancesUseCase(
            StudentRepositoryImpl.getInstance()
    );

    private final GetGroupNameByIdUseCase getGroupNameByIdUseCase = new GetGroupNameByIdUseCase(
            GroupsRepositoryImpl.getInstance()
    );

    private final ChangeStudentAttAndPointsUseCase changeStudentAttAndPointsUseCase = new ChangeStudentAttAndPointsUseCase(
            AttendanceRepositoryImpl.getINSTANCE()
    );
    private final DeleteStudentFromGroupUseCase deleteStudentFromGroupUseCase = new DeleteStudentFromGroupUseCase(
            GroupsRepositoryImpl.getInstance()
    );
    /* USE CASES */
    private String groupId;
    private List<StudentEntity> students = new ArrayList<>();
    public void update(@Nullable String id) {
        if (id == null && groupId == null) throw new IllegalStateException();
        if (groupId != null && id == null) {
            mutableStateLiveData.postValue(new State(null, null, null, false, true));
            getStudentsAttendancesUseCase.execute(groupId, status -> {
                getGroupNameByIdUseCase.execute(groupId, groupNameStatus -> {
                    if (groupNameStatus.getStatusCode() == 200 && groupNameStatus.getErrors() == null && groupNameStatus.getValue() != null) {


                        List<StudentEntity> students = status.getValue() != null ? status.getValue() : new ArrayList<>();
                        List<StudentEntity> sortedByNames = sortFullNames(students);
                        List<StudentEntity> sortedByDatesAndNames = sortAttendancesForStudents(sortedByNames);


                        this.students = status.getValue() != null ? status.getValue() : null;
                        mutableStateLiveData.postValue(new State(groupNameStatus.getValue(), status.getValue() != null ? status.getValue() : null,
                                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                                status.getErrors() == null && status.getValue() != null && !sortedByDatesAndNames.isEmpty(), false));
                    } else {
                        mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз");
                    }
                });
            });
        } else {
            groupId = id;
            mutableStateLiveData.postValue(new State(null, null, null, false, true));
            getStudentsAttendancesUseCase.execute(groupId, status -> {
                getGroupNameByIdUseCase.execute(groupId, groupNameStatus -> {
                    if (groupNameStatus.getStatusCode() == 200 && groupNameStatus.getErrors() == null && groupNameStatus.getValue() != null) {


                        List<StudentEntity> students = status.getValue() != null ? status.getValue() : new ArrayList<>();
                        List<StudentEntity> sortedByNames = sortFullNames(students);
                        List<StudentEntity> sortedByDatesAndNames = sortAttendancesForStudents(sortedByNames);


                        this.students = status.getValue() != null ? status.getValue() : null;
                        mutableStateLiveData.postValue(new State(groupNameStatus.getValue(), status.getValue() != null ? status.getValue() : null,
                                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                                status.getErrors() == null && status.getValue() != null && !sortedByDatesAndNames.isEmpty(), false));
                    } else {
                        mutableErrorLiveData.postValue("Что-то пошло не так. Попробуйте еще раз");
                    }
                });
            });
        }
    }
    public void deleteStudent(@NonNull String studentId){
        if (students.size() == 1){
            mutableErrorLiveData.postValue("В группе должен быть хотя бы 1 ученик");
            return;
        }
        deleteStudentFromGroupUseCase.execute(groupId, studentId, status -> {
            if (status.getStatusCode() == 200){
                mutableDeleteLiveData.postValue(true);
            }
            else{
                mutableDeleteLiveData.postValue(false);
            }
        });
    }
    public List<String> extractDates(List<AttendanceEntity> attendances) {
        List<String> dates = new ArrayList<>();
        sortAttendances(attendances);
        for (AttendanceEntity att : attendances) {
            dates.add(DateFormatter.getDateStringFromDate(att.getLessonTimeStart(), "MMM dd"));
        }
        return dates;
    }
    public List<StudentEntity> getStudents() {
        return this.students;
    }
    public void setAttAndPointsToStudent(AttendanceEntity attendance) {
        changeStudentAttAndPointsUseCase.execute(
                attendance.getId(),
                attendance.getLessonId(),
                attendance.getStudentId(),
                attendance.getVisited(),
                attendance.getPoints(),
                status -> {
                    if (status.getStatusCode() == 200){
                    }
                    else{
                        if(status.getErrors() != null) mutableErrorLiveData.postValue(status.getErrors().getLocalizedMessage());
                        else mutableErrorLiveData.postValue("Что-то пошло не так");
                    }
                });
    }
    public void saveGroupId(@NonNull String id) {
        groupId = id;
    }
    public String getGroupId() {
        return groupId;
    }
    private List<StudentEntity> sortAttendancesForStudents(@Nullable List<StudentEntity> students) {
        if (students == null) return new ArrayList<>();
        for (StudentEntity student : students) {
            List<AttendanceEntity> attendanceEntities = student.getAttendanceEntityList();
            attendanceEntities.sort(Comparator.comparing(AttendanceEntity::getLessonTimeStart));
        }
        return students;
    }
    private void sortAttendances(@Nullable List<AttendanceEntity> attendances) {
        if (attendances == null) return;
        attendances.sort(Comparator.comparing(AttendanceEntity::getLessonTimeStart));
    }
    private List<StudentEntity> sortFullNames(@Nullable List<StudentEntity> students){
        if (students == null) return new ArrayList<>();
        students.sort(Comparator.comparing(StudentEntity::getFullName));
        return students;

    }
    public class State {

        @Nullable
        private final String groupName;
        @Nullable
        private final List<StudentEntity> students;
        @Nullable
        private final String errorMessage;
        @NonNull
        private final Boolean isSuccess;
        @NonNull
        private final Boolean isLoading;

        public State(@Nullable String groupName, @Nullable List<StudentEntity> students, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.groupName = groupName;
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<StudentEntity> getStudents() {
            return students;
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

        @Nullable
        public String getGroupName() {
            return groupName;
        }
    }
}
