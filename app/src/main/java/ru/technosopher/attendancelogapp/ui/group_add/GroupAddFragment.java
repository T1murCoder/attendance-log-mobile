package ru.technosopher.attendancelogapp.ui.group_add;

import android.os.Bundle;
import android.text.Editable;
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
import ru.technosopher.attendancelogapp.databinding.FragmentGroupAddBinding;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.StudentListAdapter;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class GroupAddFragment extends Fragment {
    private FragmentGroupAddBinding binding;
    private GroupAddViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_add, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentGroupAddBinding.bind(view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        viewModel = new ViewModelProvider(this).get(GroupAddViewModel.class);
        StudentListAdapter adapter = new StudentListAdapter(this::addStudent, this::deleteStudent, this::changeItemSelection);
        binding.studentsRecyclerView.setLayoutManager(mLayoutManager);
        binding.studentsRecyclerView.setAdapter(adapter);
        binding.studentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.filterList(newText.trim());
                return true;
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_groupAddFragment_to_groupsFragment);
            }
        });
        binding.groupNameEt.addTextChangedListener(new OnChangeText(){
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeName(editable.toString().trim());
            }

        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.createGroup();
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
    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    private void addStudent(@NonNull String id) {viewModel.addStudent(id);}
    private void deleteStudent(@NonNull String id){
        viewModel.deleteStudent(id);
    }
    private void changeItemSelection(@NonNull List<String> args){
        viewModel.updateItemCheckedState(args.get(0), Objects.equals(args.get(1), "t"));
    }
    private void clearStudents(){
        viewModel.clearStudents();
    }
    private void subscribe(GroupAddViewModel viewModel, StudentListAdapter adapter) {
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
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT ).show();
        });
        viewModel.confirmLiveData.observe(getViewLifecycleOwner(), unused -> {
            View view = getView();
            if (view == null) return;
            Navigation.findNavController(view).navigate(R.id.action_groupAddFragment_to_groupsFragment);
        });
    }

}
