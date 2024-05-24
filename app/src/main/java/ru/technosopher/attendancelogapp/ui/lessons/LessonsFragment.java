package ru.technosopher.attendancelogapp.ui.lessons;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentLessonsBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.ui.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.Utils;


public class LessonsFragment extends Fragment {

    private NavigationBarChangeListener navigationBarChangeListener;
    private LessonsViewModel viewModel;
    private FragmentLessonsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLessonsBinding.bind(view);
        navigationBarChangeListener.changeSelectedItem(R.id.lessons);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        LessonsListAdapter adapter = new LessonsListAdapter(this::onItemOpen, this::onItemClose, this::onDelete, this::onUpload, this::onCopyLink);

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addLessonLayout.setVisibility(View.VISIBLE);
            }
        });

        binding.addLessonConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.createLesson();
            }
        });

        binding.themeEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeTheme(editable);
            }
        });

        binding.timePickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), callback, 0, 0, true);
                dialog.show();
            }

            private TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    viewModel.changeStartTime(hour, minute);
                    TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            viewModel.changeEndTime(hour, minute);
                        }
                    }, 0, 0, true);
                    dialog.show();
                }
            };
        });
        binding.datePickerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        viewModel.changeDate(i, i1, i2);
                    }
                }, DateFormatter.getActualYear(), DateFormatter.getActualMonth(), DateFormatter.getActualDay());
                dialog.show();
            }
        });
        binding.closeCreateLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addLessonLayout.setVisibility(View.GONE);
                viewModel.clearAllFields();
            }
        });
        viewModel = new ViewModelProvider(this).get(LessonsViewModel.class);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setAdapter(adapter);
        subscribe(viewModel, adapter);

    }

    private void onItemOpen(Boolean aBoolean) {
    }

    private void onItemClose(Boolean aBoolean) {
    }

    private void onDelete(@NonNull String id) {
        viewModel.deleteLesson(id);
    }

    private void onUpload(@NonNull String id) {
    }

    private void onCopyLink(@NonNull String id) {
    }

    private void subscribe(LessonsViewModel viewModel, LessonsListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (state.getLoading()) {
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(true));
                binding.recyclerView.setVisibility(Utils.visibleOrGone(false));
                binding.floatingActionButton.setVisibility(Utils.visibleOrGone(false));
            } else {
                binding.floatingActionButton.setVisibility(Utils.visibleOrGone(true));
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(false));
                binding.recyclerView.setVisibility(Utils.visibleOrGone(state.getSuccess()));
                System.out.println(state.getLessons());
                if (state.getSuccess()) adapter.updateData(state.getLessons());
                //binding..setVisibility(Utils.visibleOrGone(state.getSuccess()));
            }
        });

        viewModel.deleteLiveData.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Урок был успешно удален", Toast.LENGTH_SHORT).show();
                viewModel.update();
            } else {
                Toast.makeText(getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                viewModel.update();
            }
        });

        viewModel.addErrorLiveData.observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        viewModel.groupsLiveData.observe(getViewLifecycleOwner(), groupsState -> {
            if (groupsState.getSuccess()) {
                ArrayAdapter<ItemGroupEntity> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item, groupsState.getGroups());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.groupsNamesSpinner.setAdapter(arrayAdapter);
                binding.groupsNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ItemGroupEntity group = (ItemGroupEntity) adapterView.getItemAtPosition(i);
                        viewModel.changeGroup(group.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        viewModel.changeGroup(null);
                    }
                });
            }
        });
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
}