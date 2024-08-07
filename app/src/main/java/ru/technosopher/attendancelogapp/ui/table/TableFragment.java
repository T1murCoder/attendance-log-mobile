package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.data.source.CredentialsDataSource;
import ru.technosopher.attendancelogapp.databinding.FragmentTableBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.ui.group_manage.GroupManageFragment;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.student_add.StudentAddFragment;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationBarChangeListener.hideNavigationBar();
        binding = FragmentTableBinding.bind(view);

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "-1";
        viewModel = new ViewModelProvider(this).get(TableViewModel.class);
        viewModel.saveGroupId(id);

        StudentAttendancesAdapter attendancesAdapter = new StudentAttendancesAdapter(getContext(), true, this::setAttAndPointsToStudent, this::deleteStudentFromGroup);
        DatesAdapter datesAdapter = new DatesAdapter();

        select(binding.pointsBtn);
        unselect(binding.attendanceBtn);
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
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                viewModel.update(null);
                select(binding.attendanceBtn);
                unselect(binding.pointsBtn);
                binding.dateHeaderSpinner.setSelectedIndex(0);
                attendancesAdapter.updateState(false);
                attendancesAdapter.updateData(viewModel.getStudents());
                datesAdapter.update(viewModel.extractDates(viewModel.getStudents().get(0).getAttendanceEntityList()));
            }
        });
        binding.attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                viewModel.update(null);
                select(binding.pointsBtn);
                unselect(binding.attendanceBtn);
                binding.dateHeaderSpinner.setSelectedIndex(0);
                attendancesAdapter.updateState(true);
                attendancesAdapter.updateData(viewModel.getStudents());
                datesAdapter.update(viewModel.extractDates(viewModel.getStudents().get(0).getAttendanceEntityList()));
            }
        });

        binding.addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_tableFragment_to_groupManageFragment, GroupManageFragment.getBundle(id));
            }
        });

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.update(null);
                binding.dateHeaderSpinner.setSelectedIndex(0);
            }
        });

        String[] months = getResources().getStringArray(R.array.group_dropdown_months);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_spinner, months);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.dateHeaderSpinner.setAdapter(arrayAdapter);
        binding.dateHeaderSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                viewModel.filterGroupByMonth(getCalendarByMonth(months[position]));
            }
        });

        binding.studentsRv.setAdapter(attendancesAdapter);
        binding.datesRv.setAdapter(datesAdapter);

        subscribe(viewModel, attendancesAdapter, datesAdapter);
        viewModel.update(id);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    @Override
    public void onStart() {
        super.onStart();
        binding.dateHeaderSpinner.setSelectedIndex(0);
        UpdateSharedPreferences prefs = (UpdateSharedPreferences) requireActivity();
        CredentialsDataSource.getInstance().updateLogin(prefs.getPrefsLogin(), prefs.getPrefsPassword());
        viewModel.update(null);
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

    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }

    private void select(AppCompatButton button){
        ColorStateList colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.color_selector_unfocused);
        button.setBackgroundTintList(colorStateList);
    }
    private void unselect(AppCompatButton button){
        ColorStateList colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.color_selector_default);
        button.setBackgroundTintList(colorStateList);
    }

    private void setAttAndPointsToStudent(AttendanceEntity attendanceToStudent) {
        viewModel.setAttAndPointsToStudent(attendanceToStudent);
    }

    private void deleteStudentFromGroup(String studentId) {
        createDeletionDialog(studentId).show();
    }

    private void subscribe(TableViewModel viewModel, StudentAttendancesAdapter attendancesAdapter, DatesAdapter datesAdapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (state.getLoading()) {
                binding.tableContent.setVisibility(View.GONE);
                binding.tableErrorTv.setVisibility(View.GONE);
                binding.tableProgressBar.setVisibility(View.VISIBLE);
                binding.backWithoutLoading.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            } else {
                binding.swipe.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);
                if (state.getSuccess()) {
                    binding.tableGroupName.setText(state.getGroupName());
                    binding.tableContent.setVisibility(View.VISIBLE);
                    binding.tableProgressBar.setVisibility(View.GONE);
                    binding.backWithoutLoading.setVisibility(View.GONE);
                    binding.tableErrorTv.setVisibility(View.GONE);

                    if (state.getStudents().isEmpty()){
                        binding.buttonsAttPointsLayout.setVisibility(View.GONE);
                        binding.tableHeader.setVisibility(View.GONE);
                        binding.hsrStudentsTable.setVisibility(View.GONE);
                        binding.studentsRv.setVisibility(View.GONE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.GONE);
                        binding.tableErrorTv.setVisibility(View.VISIBLE);
                        binding.tableErrorTv.setText("У вас пока нет учеников");
                    } else if (state.getStudents().get(0).getAttendanceEntityList().isEmpty() && binding.dateHeaderSpinner.getSelectedIndex() == 0) {
                        binding.buttonsAttPointsLayout.setVisibility(View.GONE);
                        binding.tableHeader.setVisibility(View.GONE);
                        binding.hsrStudentsTable.setVisibility(View.GONE);
                        binding.studentsRv.setVisibility(View.GONE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.VISIBLE);

                        StudentsListAdapterForTable adapter = new StudentsListAdapterForTable(this::deleteStudentFromGroup);
                        binding.studentsEmptyLessonsRv.setAdapter(adapter);
                        adapter.updateData(state.getStudents());
                    } else {
                        binding.buttonsAttPointsLayout.setVisibility(View.VISIBLE);
                        binding.tableHeader.setVisibility(View.VISIBLE);
                        binding.studentsRv.setVisibility(View.VISIBLE);
                        binding.hsrStudentsTable.setVisibility(View.VISIBLE);
                        binding.studentsEmptyLessonsRv.setVisibility(View.GONE);
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
            } else {
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                viewModel.update(null);
            }
        });

        viewModel.errorLiveData.observe(getViewLifecycleOwner(), errorMsg -> {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            viewModel.update(null);
        });
    }
    private GregorianCalendar getCalendarByMonth(String month) {

        String monthLower = month.toLowerCase().trim();

        String[] months = getResources().getStringArray(R.array.months_strings);

        int monthIndex = -1;

        for (int i = 0; i < months.length; i++) {
            if (months[i].toLowerCase().equals(monthLower)) {
                monthIndex = i;
                break;
            }
        }

        if (monthIndex == -1) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.MONTH, monthIndex);

        return calendar;
    }

    private AlertDialog createDeletionDialog(@NonNull String studentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
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

}
