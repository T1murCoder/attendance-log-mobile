package ru.technosopher.attendancelogapp.ui.student_add;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentStudentsAddBinding;
import ru.technosopher.attendancelogapp.ui.group_add.GroupAddViewModel;
import ru.technosopher.attendancelogapp.ui.group_add.StudentListAdapter;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class StudentAddFragment extends Fragment {

    public static String KEY_ID = "STUDENTS_ADD_FRAGMENT";

    private FragmentStudentsAddBinding binding;

    private StudentAddViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_students_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentStudentsAddBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(StudentAddViewModel.class);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        StudentListAdapter adapter = new StudentListAdapter(this::addStudent, this::deleteStudent);

        binding.studentsRecyclerView.setLayoutManager(mLayoutManager);
        binding.studentsRecyclerView.setAdapter(adapter);

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "-1";
        viewModel.saveGroupId(id);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_studentAddFragment_to_tableFragment, TableFragment.getBundle(id));
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addStudentsToGroup();
            }
        });
        subscribe(viewModel, adapter);
        viewModel.update();
    }

    private void addStudent(@NonNull String id) {
        viewModel.addStudent(id);
        System.out.println(viewModel.selectedStudents);
    }
    private void deleteStudent(@NonNull String id){
        viewModel.deleteStudent(id);
        System.out.println(viewModel.selectedStudents);
    }
    private void clearStudents(){
        viewModel.clearStudents();
    }
    private void subscribe(StudentAddViewModel viewModel, StudentListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), studentsState -> {
            if (studentsState.getLoading()){
                binding.progressBar.setVisibility(Utils.visibleOrGone(true));
                binding.studentsRecyclerView.setVisibility(Utils.visibleOrGone(false));
                binding.errorTv.setVisibility(Utils.visibleOrGone(false));
            }else{
                binding.progressBar.setVisibility(Utils.visibleOrGone(false));
                binding.studentsRecyclerView.setVisibility(Utils.visibleOrGone(studentsState.getSuccess()));
                binding.errorTv.setVisibility(Utils.visibleOrGone(!studentsState.getSuccess()));
                if (studentsState.getSuccess()){
                    adapter.updateData(studentsState.getStudents());
                }
                else{
                    binding.errorTv.setText("Учеников без группы нет");
                }
            }
        });
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error->{
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            viewModel.update();
        });
        viewModel.confirmLiveData.observe(getViewLifecycleOwner(), unused -> {
            View view = getView();
            if (view == null) return;
            Navigation.findNavController(view).navigate(R.id.action_studentAddFragment_to_tableFragment, TableFragment.getBundle(viewModel.getGroupId()));
        });
    }
    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
}
