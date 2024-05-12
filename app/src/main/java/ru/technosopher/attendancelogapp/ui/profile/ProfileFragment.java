package ru.technosopher.attendancelogapp.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentProfileBinding;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;


public class ProfileFragment extends Fragment {

    private static final String KEY_ID = "profile_fragment";
    private FragmentProfileBinding binding;

    private ProfileViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProfileBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            TeacherEntity teacher = state.getTeacher();
            if (teacher == null) return;
            binding.profileLoginTv.setText(teacher.getUsername());
            binding.profileNameEt.setText(teacher.getName());
            binding.profileSurnameEt.setText(teacher.getSurname());
            binding.profileTelegramEt.setText(teacher.getTelegram_url() != null ? teacher.getTelegram_url() : "Provide your telegram");
            binding.profileGithubEt.setText(teacher.getGithub_url() != null ? teacher.getGithub_url() : "Provide your github");

            // TODO (validate link)
            if (teacher.getPhoto_url() != null)
                Picasso.get().load(teacher.getPhoto_url()).into(binding.profileAvatarIv);

            //TODO (Do something with notifications)
        });

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : null;
        if (id == null) {
            // THIS SHOULD NEVER BE HAPPEN
            throw new IllegalStateException("ID IS NULL");
        }
        viewModel.load(id);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;


    }
}