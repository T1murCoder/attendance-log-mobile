package ru.technosopher.attendancelogapp.ui.groups;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogapp.databinding.DialogAddGroupBinding;
import ru.technosopher.attendancelogapp.databinding.DialogCreateLessonBinding;
import ru.technosopher.attendancelogapp.databinding.FragmentGroupsBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.ui.lessons.LessonsFragment;
import ru.technosopher.attendancelogapp.ui.lessons.LessonsViewModel;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;
import ru.technosopher.attendancelogapp.ui.utils.Utils;


public class GroupsFragment extends Fragment {

    private final static String TAG = "GroupsFragment";
    private BottomGroupCreateDialog dialog;
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
        dialog = new BottomGroupCreateDialog(viewModel);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getChildFragmentManager(), TAG);
            }
        });

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.update();
            }
        });
        subscribe(viewModel, adapter);
    }
    @Override
    public void onDestroyView() {
        binding = null;
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
    @Override
    public void onStart() {
        super.onStart();
        UpdateSharedPreferences prefs = (UpdateSharedPreferences) requireActivity();
        CredentialsDataSource.getInstance().updateLogin(prefs.getPrefsLogin(), prefs.getPrefsPassword());
        viewModel.update();
    }
    private void subscribe(GroupsViewModel viewModel, GroupsListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state->{
            if (state.getLoading()){
                binding.groupsContent.setVisibility(View.GONE);
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(state.getLoading()));
                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            }else{
                binding.swipe.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);
                binding.groupsContent.setVisibility(View.VISIBLE);
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
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), msg ->{
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            viewModel.changeName(null);
            dialog.binding.groupNameDialogEt.setText(null);
            dialog.dismiss();
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
    private AlertDialog createDeletionDialog(@NonNull String group_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
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

    public static class BottomGroupCreateDialog extends BottomSheetDialogFragment {
        private DialogAddGroupBinding binding;
        private List<ItemGroupEntity> groups = Collections.singletonList(new ItemGroupEntity("-1", "Не выбрано"));
        private final GroupsViewModel groupsViewModel;
        public BottomGroupCreateDialog(GroupsViewModel groupsViewModel){
            this.groupsViewModel = groupsViewModel;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = DialogAddGroupBinding.bind(inflater.inflate(R.layout.dialog_add_group, container, false));
            binding.addGroupConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    groupsViewModel.createGroup();
                    groupsViewModel.changeName(null);
                    binding.groupNameDialogEt.setText(null);
                    dismiss();
                }
            });
            binding.groupNameDialogEt.addTextChangedListener(new OnChangeText() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    groupsViewModel.changeName(String.valueOf(editable));
                }
            });

            return binding.getRoot();
        }
        @Override
        public void onDestroyView() {
            binding = null;
            super.onDestroyView();
        }
    }
}