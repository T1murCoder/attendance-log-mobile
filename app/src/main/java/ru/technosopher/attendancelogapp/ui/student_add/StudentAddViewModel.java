package ru.technosopher.attendancelogapp.ui.student_add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.technosopher.attendancelogapp.data.GroupsRepositoryImpl;
import ru.technosopher.attendancelogapp.data.StudentRepositoryImpl;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.entities.Status;
import ru.technosopher.attendancelogapp.domain.groups.AddStudentsToGroupUseCase;
import ru.technosopher.attendancelogapp.domain.groups.CreateGroupUseCase;
import ru.technosopher.attendancelogapp.domain.students.GetStudentsListUseCase;
import ru.technosopher.attendancelogapp.ui.group_add.GroupAddViewModel;

public class StudentAddViewModel extends ViewModel {
    /* LIVEDATA */
    private final MutableLiveData<StudentsState> mutableStateLiveData = new MutableLiveData<>();

    public final LiveData<StudentsState> stateLiveData = mutableStateLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;

    /* LIVEDATA */
    public List<ItemStudentEntity> selectedStudents = new ArrayList<>();
    /* USE CASES */
    private final GetStudentsListUseCase getStudentsListUseCase = new GetStudentsListUseCase(
            StudentRepositoryImpl.getInstance()
    );
    private final AddStudentsToGroupUseCase addStudentsToGroupUseCase = new AddStudentsToGroupUseCase(
            GroupsRepositoryImpl.getInstance()
    );

    /* USE CASES */
    private String id;


    /* LOGIC */
    public void addStudentsToGroup() {
        if (id != null){
            addStudentsToGroupUseCase.execute(id, selectedStudents, status -> {
                if (status.getStatusCode() == 200) {
                    mutableConfirmLiveData.postValue(null);
                }
                else{

                    System.out.println(status.getErrors());
                }
            });
        }else{
            mutableErrorLiveData.setValue("Group id is null");
        }
    }

    public void loadStudents() {
        mutableStateLiveData.postValue(new StudentsState(null, null, false, true));
        getStudentsListUseCase.execute(status -> {
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private StudentsState fromStatus(Status<List<ItemStudentEntity>> status) {
        return new StudentsState(
                status.getValue(),
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getErrors() == null && status.getValue() != null, false);
    }
    public void addStudent(@NonNull String id) {
        selectedStudents.add(new ItemStudentEntity(
                id,
                ""
        ));
    }
    public void deleteStudent(@NonNull String id) {
        if (selectedStudents != null) {
            Iterator<ItemStudentEntity> itr = selectedStudents.iterator();
            while (itr.hasNext()) {
                ItemStudentEntity student = (ItemStudentEntity) itr.next();
                if (student.getId().equals(id)) {
                    itr.remove();
                    break;
                }
            }
        }
    }
    public void clearStudents() {
        selectedStudents = new ArrayList<>();
    }
    public void saveGroupId(@NonNull String id) {
        this.id = id;
    }
    public String getGroupId() {
        return this.id;
    }
    public class StudentsState {
        @Nullable
        private final List<ItemStudentEntity> students;
        @Nullable
        private final String errorMessage;
        @NonNull
        private final Boolean isSuccess;
        @NonNull
        private final Boolean isLoading;

        public StudentsState(@Nullable List<ItemStudentEntity> students, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<ItemStudentEntity> getStudents() {
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
    }
}
