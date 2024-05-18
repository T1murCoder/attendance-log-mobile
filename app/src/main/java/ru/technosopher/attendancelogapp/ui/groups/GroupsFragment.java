package ru.technosopher.attendancelogapp.ui.groups;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import okhttp3.internal.Util;
import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentGroupsBinding;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.utils.Utils;


public class GroupsFragment extends Fragment {

    FragmentGroupsBinding binding;
    private GroupsViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentGroupsBinding.bind(view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        GroupsListAdapter adapter = new GroupsListAdapter(this::openGroup);
        viewModel = new ViewModelProvider(this).get(GroupsViewModel.class);

        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_groupsFragment_to_groupAddFragment);
            }
        });
        subscribe(viewModel, adapter);

    }

    private void subscribe(GroupsViewModel viewModel, GroupsListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state->{

            binding.recyclerView.setVisibility(Utils.visibleOrGone(state.getSuccess()));
            binding.groupsErrorMessage.setVisibility(Utils.visibleOrGone(!state.getSuccess()));
            binding.groupsErrorMessage.setText(state.getErrorMessage());

            if (state.getSuccess()){
                adapter.updateData(state.getGroups());
            }
        });
    }

    private void openGroup(@NonNull String id) {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_groupsFragment_to_tableFragment, TableFragment.getBundle(id));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}