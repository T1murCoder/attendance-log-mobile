package ru.technosopher.attendancelogapp.ui.lessons;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import ru.technosopher.attendancelogapp.databinding.DialogCreateLessonBinding;
import ru.technosopher.attendancelogapp.databinding.FragmentLessonsBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class LessonsFragment extends Fragment{
    public final static String TAG = "LessonsFragment";
    private NavigationBarChangeListener navigationBarChangeListener;
    private LessonsViewModel viewModel;
    private FragmentLessonsBinding binding;
    private BottomLessonCreateDialog dialog;
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

        LessonsListAdapter adapter = new LessonsListAdapter(getContext(), this::checkQrCodeIsAlive, this::onDelete, this::onOpenJournal);
        viewModel = new ViewModelProvider(this).get(LessonsViewModel.class);
        dialog = new BottomLessonCreateDialog(viewModel);
        binding.recyclerView.setAdapter(adapter);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getChildFragmentManager(), TAG);
            }
        });

        binding.openEndedLessons.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                binding.noLessonsTv.setVisibility(View.GONE);
                adapter.updateData(viewModel.getEndedLessons());
                adapter.notifyDataSetChanged();
                binding.endedLessonsBackLayout.setVisibility(View.VISIBLE);
                binding.openEndedLessons.setVisibility(View.GONE);
                binding.floatingActionButton.setVisibility(View.GONE);

            }
        });

        binding.backToCurrentLessons.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                adapter.updateData(viewModel.getCurrentLessons());
                adapter.notifyDataSetChanged();
                binding.endedLessonsBackLayout.setVisibility(View.GONE);
                binding.openEndedLessons.setVisibility(View.VISIBLE);
                binding.floatingActionButton.setVisibility(View.VISIBLE);
                if (viewModel.getCurrentLessons().isEmpty() || viewModel.getCurrentLessons() == null){
                    binding.noLessonsTv.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.update();
                binding.openEndedLessons.setVisibility(View.VISIBLE);
                binding.endedLessonsBackLayout.setVisibility(View.GONE);
            }
        });
        subscribe(viewModel, adapter);
    }

//    private LessonsListAdapter createNewAdapter(){
//        return new LessonsListAdapter(getContext(), this::checkQrCodeIsAlive, this::onDelete, this::onOpenJournal);
//    }
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
    private void checkQrCodeIsAlive(@NonNull String lessonId){
        viewModel.checkQRCodeIsAlive(lessonId);
    }
    private void onOpenJournal(String id){
        View view = getView();
        if (view == null) return ;
        Navigation.findNavController(view).navigate(R.id.action_lessonsFragment_to_dummyAttendancesFragment, DummyAttendancesFragment.getBundle(id));

    }
    private void onDelete(@NonNull String id) {
        viewModel.deleteLesson(id);
    }
    private void subscribe(LessonsViewModel viewModel, LessonsListAdapter adapter) {

        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (state.getLoading()) {
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(true));
                binding.recyclerView.setVisibility(Utils.visibleOrGone(false));

                binding.openEndedLessons.setVisibility(View.GONE);
                binding.endedLessonsBackLayout.setVisibility(View.GONE);

                binding.floatingActionButton.setVisibility(Utils.visibleOrGone(false));
                binding.noLessonsTv.setVisibility(View.GONE);

                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            }
            else {
                binding.floatingActionButton.setVisibility(Utils.visibleOrGone(true));
                binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(false));
                binding.recyclerView.setVisibility(Utils.visibleOrGone(state.getSuccess()));
                binding.openEndedLessons.setVisibility(Utils.visibleOrGone(state.getSuccess()));

                binding.swipe.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);

                if (state.getSuccess()){
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    if (state.getLessons().isEmpty() && viewModel.getEndedLessons().isEmpty()){
                        binding.openEndedLessons.setVisibility(View.GONE);
                        binding.endedLessonsBackLayout.setVisibility(View.GONE);
                        binding.noLessonsTv.setVisibility(View.VISIBLE);
                    }
                    else if (!state.getLessons().isEmpty() && viewModel.getEndedLessons().isEmpty()){
                        binding.openEndedLessons.setVisibility(View.GONE);
                        binding.endedLessonsBackLayout.setVisibility(View.GONE);
                        binding.noLessonsTv.setVisibility(View.GONE);
                    } else if (state.getLessons().isEmpty() && !viewModel.getEndedLessons().isEmpty()) {
                        binding.openEndedLessons.setVisibility(View.VISIBLE);
                        binding.noLessonsTv.setVisibility(View.VISIBLE);
                    }
                    adapter.updateData(state.getLessons());
                    //adapter.setHasStableIds(true);
                }
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
//            binding.openEndedLessons.setVisibility(View.VISIBLE);
//            binding.endedLessonsBackLayout.setVisibility(View.GONE);
        });

        viewModel.addErrorLiveData.observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            viewModel.clearAllFields();
            viewModel.changeGroup(null);
            dialog.binding.datePickerTv.setText("Выберите дату");
            dialog.binding.timePickerTv.setText("Выберите время");
            dialog.binding.groupsNamesSpinner.setSelected(false);
            dialog.binding.groupsNamesSpinner.setSelection(0);
            dialog.binding.themeEt.setText(null);
            dialog.dismiss();
        });

        viewModel.groupsLiveData.observe(getViewLifecycleOwner(), groupsState -> {
            if (groupsState.getSuccess()) {
                dialog.saveGroupsData(groupsState.getGroups());
            }
        });

        viewModel.itemQrCodeLiveData.observe(getViewLifecycleOwner(), qrCode -> {
            if (qrCode != null){
                adapter.updateItemQrCode(qrCode);
            }
        });

        viewModel.errorsLiveData.observe(getViewLifecycleOwner(), qrCodeState ->{
            Toast.makeText(getContext(), qrCodeState.getErrorMsg(), Toast.LENGTH_SHORT).show();
            adapter.updateItemQrCode(new QrCodeEntity("", qrCodeState.getLessonId(), null, null));
        });
    }
    public static class BottomLessonCreateDialog extends BottomSheetDialogFragment {
        private  DialogCreateLessonBinding binding;
        private List<ItemGroupEntity> groups = Collections.singletonList(new ItemGroupEntity("-1", "Не выбрано"));
        private final LessonsViewModel lessonsViewModel;
        public BottomLessonCreateDialog(LessonsViewModel lessonsViewModel){
            this.lessonsViewModel = lessonsViewModel;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = DialogCreateLessonBinding.bind(inflater.inflate(R.layout.dialog_create_lesson, container, false));


            binding.addLessonConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lessonsViewModel.createLesson();
                    lessonsViewModel.clearAllFields();
                    lessonsViewModel.changeGroup(null);
                    binding.datePickerTv.setText("Выберите дату");
                    binding.timePickerTv.setText("Выберите время");
                    binding.groupsNamesSpinner.setSelection(0);
                    binding.themeEt.setText(null);
                    dismiss();
                }
            });
            binding.themeEt.addTextChangedListener(new OnChangeText() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    lessonsViewModel.changeTheme(editable);
                }
            });
            binding.timePickerTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog dialog = new TimePickerDialog(getContext(), R.style.DialogTheme, callback, 0, 0, true);
                    dialog.show();
                }
                private TimePickerDialog.OnTimeSetListener callback = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                        lessonsViewModel.changeStartTime(hour, minute);
                        TimePickerDialog dialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                lessonsViewModel.changeEndTime(hour, minute);
                                binding.timePickerTv.setText(lessonsViewModel.getFullTime());
                            }
                        }, 0, 0, true);
                        dialog.show();
                    }
                };
            });
            binding.datePickerTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            lessonsViewModel.changeDate(i, i1, i2);
                            binding.datePickerTv.setText(lessonsViewModel.getDate());
                        }
                    }, DateFormatter.getActualYear(), DateFormatter.getActualMonth(), DateFormatter.getActualDay());
                    dialog.show();
                }
            });
            binding.groupsNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ItemGroupEntity group = (ItemGroupEntity) adapterView.getItemAtPosition(i);
                    lessonsViewModel.changeGroup(group.getId());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    lessonsViewModel.changeGroup(null);
                }
            });

            ArrayAdapter<ItemGroupEntity> arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_spinner, this.groups);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.groupsNamesSpinner.setAdapter(arrayAdapter);

            return binding.getRoot();
        }
        public void saveGroupsData(List<ItemGroupEntity> groups){
            this.groups = new ArrayList<>();
            this.groups.add(0, new ItemGroupEntity("-1", "Не выбрано"));
            this.groups.addAll(groups);
        }
        @Override
        public void onDestroyView() {
            binding = null;
            super.onDestroyView();
        }
    }
}