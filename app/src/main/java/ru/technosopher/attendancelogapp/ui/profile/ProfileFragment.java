package ru.technosopher.attendancelogapp.ui.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentProfileBinding;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;


public class ProfileFragment extends Fragment {

    private NavigationBarChangeListener navigationBarChangeListener;
    private UpdateSharedPreferences prefs;
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
        navigationBarChangeListener.changeSelectedItem(R.id.profile);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        binding.profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logout();
            }
        });


        subscribe(viewModel);
        viewModel.loadPrefs(
                prefs.getPrefsId(),
                prefs.getPrefsLogin(),
                prefs.getPrefsName(),
                prefs.getPrefsSurname(),
                prefs.getPrefsTelegram(),
                prefs.getPrefsGithub(),
                prefs.getPrefsPhotoUrl()
        );

    }

    private void subscribe(ProfileViewModel viewModel) {
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

        });
        viewModel.logoutLiveData.observe(getViewLifecycleOwner(), unused -> {
            prefs.clearAll();
            View view = getView();
            if (view == null) return;
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
//            Navigation.findNavController(view).navigate(
//                    R.id.action_profileFragment_to_loginFragment,
//                    null,
//                    new NavOptions.Builder().setPopUpTo(R.id.profileFragment, true).build());
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
            prefs = (UpdateSharedPreferences) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
}