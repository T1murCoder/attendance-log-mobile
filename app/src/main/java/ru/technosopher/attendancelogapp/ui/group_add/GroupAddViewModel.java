package ru.technosopher.attendancelogapp.ui.group_add;

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
import ru.technosopher.attendancelogapp.domain.groups.CreateGroupUseCase;
import ru.technosopher.attendancelogapp.domain.students.GetStudentsListUseCase;

public class GroupAddViewModel extends ViewModel {

    /* LIVEDATA */
    private final MutableLiveData<StudentsState> mutableStateLiveData = new MutableLiveData<>();

    public final LiveData<StudentsState> stateLiveData = mutableStateLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;

    /* LIVEDATA */
    public List<ItemStudentEntity> selectedStudents = new ArrayList<>();
    @Nullable
    private String name;
    /* USE CASES */
    private final GetStudentsListUseCase getStudentsListUseCase = new GetStudentsListUseCase(
            StudentRepositoryImpl.getInstance()
    );
    private final CreateGroupUseCase createGroupUseCase = new CreateGroupUseCase(
            GroupsRepositoryImpl.getInstance()
    );

    /* USE CASES */


    /* LOGIC */
    public void createGroup() {
        if (name == null) {
            mutableErrorLiveData.postValue("Введите имя группы");
        } else {
            if (selectedStudents != null && !selectedStudents.isEmpty()){
                createGroupUseCase.execute(name, selectedStudents, status -> {
                    System.out.println(status.getStatusCode());
                    if (status.getStatusCode() == 200) {
                        mutableConfirmLiveData.postValue(null);
                    }
                });
            }else{
                mutableErrorLiveData.postValue("Добавьте учеников");
            }
        }
    }

    public void update() {
        mutableStateLiveData.postValue(new StudentsState(null, null, false, true));
        getStudentsListUseCase.execute(status -> {
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private StudentsState fromStatus(Status<List<ItemStudentEntity>> status) {
        return new StudentsState(
                status.getValue(),
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getErrors() == null && status.getValue() != null && !status.getValue().isEmpty(), false);
    }
    public void addStudent(@NonNull String id) {
        selectedStudents.add(new ItemStudentEntity(
                id,
                "",
                "",
                ""));
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
    public void changeName(String string) {
        this.name = string;
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

