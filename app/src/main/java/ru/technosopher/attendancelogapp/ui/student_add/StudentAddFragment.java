package ru.technosopher.attendancelogapp.ui.student_add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Objects;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogapp.databinding.FragmentStudentsAddBinding;
import ru.technosopher.attendancelogapp.ui.utils.StudentListAdapter;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;
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
        StudentListAdapter adapter = new StudentListAdapter(this::addStudent, this::deleteStudent, this::changeItemSelection);

        binding.studentsRecyclerView.setLayoutManager(mLayoutManager);
        binding.studentsRecyclerView.setAdapter(adapter);

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "-1";
        viewModel.saveGroupId(id);

        binding.studentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.filterList(newText);
                return true;
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                //Navigation.findNavController(view).navigate(R.id.action_studentAddFragment_to_tableFragment, TableFragment.getBundle(id));
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.addStudentsToGroup();
            }
        });

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.update();
            }
        });
        subscribe(viewModel, adapter);
        viewModel.update();
    }
    @Override
    public void onStart() {
        super.onStart();
        UpdateSharedPreferences prefs = (UpdateSharedPreferences) requireActivity();
        CredentialsDataSource.getInstance().updateLogin(prefs.getPrefsLogin(), prefs.getPrefsPassword());
        viewModel.update();
    }
    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    private void subscribe(StudentAddViewModel viewModel, StudentListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), studentsState -> {
            if (studentsState.getLoading()){
                binding.progressBar.setVisibility(Utils.visibleOrGone(true));
                binding.studentsRecyclerView.setVisibility(Utils.visibleOrGone(false));
                binding.errorTv.setVisibility(Utils.visibleOrGone(false));
                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            }else{
                binding.swipe.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);
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
            //Navigation.findNavController(view).navigate(R.id.action_studentAddFragment_to_tableFragment, TableFragment.getBundle(viewModel.getGroupId()));
        });
    }
    private void addStudent(@NonNull String id) {viewModel.addStudent(id);}
    private void deleteStudent(@NonNull String id){
        viewModel.deleteStudent(id);
    }
    private void changeItemSelection(@NonNull List<String> args){
        viewModel.updateItemCheckedState(args.get(0), Objects.equals(args.get(1), "t"));
    }
}
