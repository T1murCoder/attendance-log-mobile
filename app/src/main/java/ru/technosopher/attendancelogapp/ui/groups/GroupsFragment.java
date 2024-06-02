package ru.technosopher.attendancelogapp.ui.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentGroupsBinding;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.utils.Utils;


public class GroupsFragment extends Fragment {
    private NavigationBarChangeListener navigationBarChangeListener;
    private FragmentGroupsBinding binding;
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
        navigationBarChangeListener.showNavigationBar();
        navigationBarChangeListener.changeSelectedItem(R.id.groups);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        GroupsListAdapter adapter = new GroupsListAdapter(this::openGroup, this::deleteGroup);
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
            if (state.getLoading()){
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(state.getLoading()));
            }else{
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(state.getLoading()));
                binding.recyclerView.setVisibility(Utils.visibleOrGone(state.getSuccess()));
                binding.groupsErrorMessage.setVisibility(Utils.visibleOrGone(!state.getSuccess()));
                binding.groupsErrorMessage.setText(state.getErrorMessage());
                if (state.getSuccess()){
                    adapter.updateData(state.getGroups());
                    binding.noGroupsTv.setVisibility(Utils.visibleOrGone(false));
                    if( state.getGroups() != null){
                        if(state.getGroups().isEmpty()) binding.noGroupsTv.setVisibility(Utils.visibleOrGone(true));
                    }else{
                        binding.noGroupsTv.setVisibility(Utils.visibleOrGone(true));
                    }
                }
            }
        });
        viewModel.deleteLiveData.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Группа успешно удалена", Toast.LENGTH_SHORT).show();
                viewModel.update();
            }
            else{
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                viewModel.update();
            }
        });
    }

    private void openGroup(@NonNull String id) {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_groupsFragment_to_tableFragment, TableFragment.getBundle(id));
    }

    private void deleteGroup(@NonNull String id){
        createDeletionDialog(id).show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigationBarChangeListener = (NavigationBarChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    private AlertDialog createDeletionDialog(@NonNull String group_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Удалить группу?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.deleteGroup(group_id);
                    }
                })
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}