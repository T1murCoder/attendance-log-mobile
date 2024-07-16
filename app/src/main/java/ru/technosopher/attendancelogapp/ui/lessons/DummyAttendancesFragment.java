package ru.technosopher.attendancelogapp.ui.lessons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentDummyAttendanceBinding;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class DummyAttendancesFragment extends Fragment {

    private static final String KEY_ID = "DUMMY_FRAGMENT";
    private FragmentDummyAttendanceBinding binding;
    private DummyAttendancesViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy_attendance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDummyAttendanceBinding.bind(view);

        DummyAdapter adapter = new DummyAdapter();
        viewModel = new ViewModelProvider(this).get(DummyAttendancesViewModel.class);
        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "Something went wrong";
        binding.studentRv.setAdapter(adapter);
        subscribe(viewModel, adapter);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_dummyAttendancesFragment_to_lessonsFragment);
            }
        });
        viewModel.load(id);
    }

    private void subscribe(DummyAttendancesViewModel viewModel, DummyAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state->{
            if (!state.getLoading()) {
                binding.studentRv.setVisibility(Utils.visibleOrGone(state.getIsSuccess()));
                if (state.getIsSuccess()) {
                    adapter.updateData(state.getStudents());
                }
            }
        });
    }
    public static Bundle getBundle(@NonNull String id){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
