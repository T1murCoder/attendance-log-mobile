package ru.technosopher.attendancelogapp.ui.student_add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import ru.technosopher.attendancelogapp.data.repository.GroupsRepositoryImpl;
import ru.technosopher.attendancelogapp.data.repository.StudentRepositoryImpl;
import ru.technosopher.attendancelogapp.data.utils.Mapper;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.domain.groups.PutStudentsToGroupUseCase;
import ru.technosopher.attendancelogapp.domain.students.GetStudentsListUseCase;
import ru.technosopher.attendancelogapp.ui.utils.ItemStudentEntityModel;

public class StudentAddViewModel extends ViewModel {
    /* LIVEDATA */
    private final MutableLiveData<StudentsState> mutableStateLiveData = new MutableLiveData<>();

    public final LiveData<StudentsState> stateLiveData = mutableStateLiveData;
    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;
    private final MutableLiveData<Void> mutableConfirmLiveData = new MutableLiveData<>();
    public final LiveData<Void> confirmLiveData = mutableConfirmLiveData;
    /* LIVEDATA */
    /* USE CASES */
    private final GetStudentsListUseCase getStudentsListUseCase = new GetStudentsListUseCase(
            StudentRepositoryImpl.getInstance()
    );
    private final PutStudentsToGroupUseCase putStudentsToGroupUseCase = new PutStudentsToGroupUseCase(
            GroupsRepositoryImpl.getInstance()
    );
    /* USE CASES */
    private String id;

    private List<ItemStudentEntityModel> selectedStudents = new ArrayList<>();
    private List<ItemStudentEntityModel> studentModelList = new ArrayList<>();
    /* LOGIC */
    public void addStudentsToGroup() {
        if (id != null){
            List<ItemStudentEntity> tmp = selectedStudents.stream().map(Mapper::fromModelToEntity).collect(Collectors.toList());
            putStudentsToGroupUseCase.execute(id, tmp, status -> {
                if (status.getStatusCode() == 200) {
                    mutableConfirmLiveData.postValue(null);
                }
                else{
                    mutableErrorLiveData.setValue("Что-то пошло не так. Попробуйте ещё раз");
                }
            });
        }else{
            mutableErrorLiveData.setValue("Group id is null");
        }
    }

    public void update(){
        loadVacantStudents();
    }
    private void loadVacantStudents() {
        mutableStateLiveData.postValue(new StudentsState(null, null, false, true));
        getStudentsListUseCase.execute(status -> {
            List<ItemStudentEntityModel> modelList = status.getValue() != null ? status.getValue().stream().map(Mapper::fromEntityToModel).collect(Collectors.toList()) : new ArrayList<>();
            studentModelList = new ArrayList<>(modelList);
            mutableStateLiveData.postValue(new StudentsState(
                    studentModelList,
                    status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                    status.getErrors() == null && status.getValue() != null && !status.getValue().isEmpty(), false)
            );
        });
    }


    public void filterList(String text) {
        List<ItemStudentEntityModel> filteredList = studentModelList != null ? studentModelList.stream().filter(
                itemStudentEntity -> itemStudentEntity.getItemStudent().getFullName().toLowerCase().contains(text.toLowerCase())
        ).collect(Collectors.toList()) : new ArrayList<>();
        mutableStateLiveData.postValue(new StudentsState(
                filteredList, null, true, false));
    }
    public void addStudent(@NonNull String id) {
        selectedStudents.add(new ItemStudentEntityModel(
                id, null, true)
        );
    }
    public void deleteStudent(@NonNull String id) {
        if (selectedStudents != null) {
            Iterator<ItemStudentEntityModel> itr = selectedStudents.iterator();
            while (itr.hasNext()) {
                ItemStudentEntityModel student = (ItemStudentEntityModel) itr.next();
                if (student.getId().equals(id)) {
                    itr.remove();
                    break;
                }
            }
        }
    }

    public void updateItemCheckedState(String id, boolean isChecked) {
        for (ItemStudentEntityModel item : studentModelList) {
            if (item.getId().equals(id)) {
                item.setChecked(isChecked);
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
        private final List<ItemStudentEntityModel> students;
        @Nullable
        private final String errorMessage;
        @NonNull
        private final Boolean isSuccess;
        @NonNull
        private final Boolean isLoading;

        public StudentsState(@Nullable List<ItemStudentEntityModel> students, @Nullable String errorMessage, @NonNull Boolean isSuccess, @NonNull Boolean isLoading) {
            this.students = students;
            this.errorMessage = errorMessage;
            this.isSuccess = isSuccess;
            this.isLoading = isLoading;
        }

        @Nullable
        public List<ItemStudentEntityModel> getStudents() {
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
