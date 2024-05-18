package ru.technosopher.attendancelogapp.ui.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import okhttp3.internal.Util;
import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentGroupAddBinding;
import ru.technosopher.attendancelogapp.databinding.FragmentGroupsBinding;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class GroupAddFragment extends Fragment {

    FragmentGroupAddBinding binding;
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
        StudentListAdapter adapter = new StudentListAdapter(this::addStudent);
        viewModel = new ViewModelProvider(this).get(GroupAddViewModel.class);
        binding.studentsRecyclerView.setLayoutManager(mLayoutManager);
        binding.studentsRecyclerView.setAdapter(adapter);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_groupAddFragment_to_groupsFragment);
            }
        });
        subscribe(viewModel, adapter);
        viewModel.load();



    }

    private void addStudent(String s) {
        // todo(ADD STUDENTS TO VIEW MODEL)
    }

    private void subscribe(GroupAddViewModel viewModel, StudentListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            binding.studentsRecyclerView.setVisibility(Utils.visibleOrGone(state.getSuccess()));
            binding.errorTv.setVisibility(Utils.visibleOrGone(!state.getSuccess()));
            if (state.getSuccess()){
                adapter.updateData(state.getStudents());
            }
            else{
                binding.errorTv.setText(state.getErrorMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
