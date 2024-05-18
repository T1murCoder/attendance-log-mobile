package ru.technosopher.attendancelogapp.ui.table;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentProfileBinding;
import ru.technosopher.attendancelogapp.databinding.FragmentTableBinding;
import ru.technosopher.attendancelogapp.ui.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.UpdateSharedPreferences;
import ru.technosopher.attendancelogapp.ui.profile.ProfileViewModel;

public class TableFragment extends Fragment {

    private static final String KEY_ID = "TABLE_FRAGMENT";
    private FragmentTableBinding binding;

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
        //navigationBarChangeListener.hideNavigationBar();
        binding = FragmentTableBinding.bind(view);

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : "Something went wrong";

        binding.testIdTv.setText(id);
        //TODO(VIEWMODEL and table :/)
        //viewModel = new ViewModelProvider(this).get(TableViewModel.class);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            navigationBarChangeListener = (NavigationBarChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public static Bundle getBundle(@NonNull String id){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
}
