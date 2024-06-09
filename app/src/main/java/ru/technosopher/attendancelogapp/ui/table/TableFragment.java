package ru.technosopher.attendancelogapp.ui.table;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentTableBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.student_add.StudentAddFragment;

public class TableFragment extends Fragment {

    private static final String KEY_ID = "TABLE_FRAGMENT";
    private FragmentTableBinding binding;
    private TableViewModel viewModel;
    private NavigationBarChangeListener navigationBarChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationBarChangeListener.hideNavigationBar();
        binding = FragmentTableBinding.bind(view);

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "Something went wrong";
        viewModel = new ViewModelProvider(this).get(TableViewModel.class);
        viewModel.saveGroupId(id);

        StudentAttendancesAdapter attendancesAdapter = new StudentAttendancesAdapter(getContext(), true, this::setAttAndPointsToStudent, this::deleteStudentFromGroup);
        DatesAdapter datesAdapter = new DatesAdapter();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_tableFragment_to_groupsFragment);
            }
        });

        binding.backWithoutLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_tableFragment_to_groupsFragment);
            }
        });

        binding.pointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.update(null);
                attendancesAdapter.updateState(false);
                attendancesAdapter.updateData(viewModel.getStudents());
                datesAdapter.update(viewModel.extractDates(viewModel.getStudents().get(0).getAttendanceEntityList()));
            }
        });
        binding.attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.update(null);
                attendancesAdapter.updateState(true);
                attendancesAdapter.updateData(viewModel.getStudents());
                datesAdapter.update(viewModel.extractDates(viewModel.getStudents().get(0).getAttendanceEntityList()));
            }
        });

        binding.addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_tableFragment_to_studentAddFragment, StudentAddFragment.getBundle(id));
            }
        });

        binding.studentsRv.setAdapter(attendancesAdapter);
        binding.datesRv.setAdapter(datesAdapter);

        subscribe(viewModel, attendancesAdapter, datesAdapter);
        viewModel.update(id);
    }

    private void setAttAndPointsToStudent(AttendanceEntity attendanceToStudent){
        viewModel.setAttAndPointsToStudent(attendanceToStudent);
    }

    private void deleteStudentFromGroup(String studentId){
        createDeletionDialog(studentId).show();
    }

    private void subscribe(TableViewModel viewModel, StudentAttendancesAdapter attendancesAdapter, DatesAdapter datesAdapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (state.getLoading()) {
                binding.tableContent.setVisibility(View.GONE);
                binding.tableErrorTv.setVisibility(View.GONE);
                binding.tableProgressBar.setVisibility(View.VISIBLE);
                binding.backWithoutLoading.setVisibility(View.VISIBLE);
            } else {
                if (state.getSuccess()) {
                    binding.tableGroupName.setText(state.getGroupName());
                    binding.tableContent.setVisibility(View.VISIBLE);
                    binding.tableProgressBar.setVisibility(View.GONE);
                    binding.backWithoutLoading.setVisibility(View.GONE);
                    binding.tableErrorTv.setVisibility(View.GONE);

                    if (state.getStudents().get(0).getAttendanceEntityList().isEmpty()){
                        binding.buttonsAttPointsLayout.setVisibility(View.GONE);
                        binding.datesLayout.setVisibility(View.GONE);
                        binding.rvsContent.setVisibility(View.GONE);
                        binding.studentsRv.setVisibility(View.GONE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.VISIBLE);

                        StudentsListAdapterForTable adapter = new StudentsListAdapterForTable(this::deleteStudentFromGroup);
                        binding.studentsEmptyLessonsRv.setAdapter(adapter);
                        adapter.updateData(state.getStudents());
                    }
                    else{
                        binding.buttonsAttPointsLayout.setVisibility(View.VISIBLE);
                        binding.calendarHeaderLayout.setVisibility(View.VISIBLE);
                        binding.studentsRv.setVisibility(View.VISIBLE);
                        binding.rvsContent.setVisibility(View.VISIBLE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.GONE);
                        binding.dateHeader.setText(DateFormatter.getDateStringFromDate(state.getStudents().get(0).getAttendanceEntityList().get(0).getLessonTimeStart(), "MMM yyyy"));
                        attendancesAdapter.updateData(state.getStudents());
                        datesAdapter.update(viewModel.extractDates(state.getStudents().get(0).getAttendanceEntityList()));
                    }
                }
            }
        });

        viewModel.deleteLiveData.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Ученик успешно удален", Toast.LENGTH_SHORT).show();
                viewModel.update(null);
            }
            else{
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                viewModel.update(null);
            }
        });

        viewModel.errorLiveData.observe(getViewLifecycleOwner(), errorMsg ->{
            binding.tableErrorTv.setVisibility(View.VISIBLE);
            binding.tableErrorTv.setText(errorMsg);
        });
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

    private AlertDialog createDeletionDialog(@NonNull String studentId){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Удалить ученика?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.deleteStudent(studentId);
                    }
                })
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
}
