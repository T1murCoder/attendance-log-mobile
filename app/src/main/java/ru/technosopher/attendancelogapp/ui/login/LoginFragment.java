package ru.technosopher.attendancelogapp.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Objects;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentLoginBinding;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class LoginFragment extends Fragment{
    private UpdateSharedPreferences prefs;
    private NavigationBarChangeListener navigationBarChangeListener;
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        try {
            navigationBarChangeListener.hideNavigationBar();
        } catch (ClassCastException ignored) {}

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.confirm();
            }
        });

        binding.loginLoginEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeLogin(editable.toString().trim());
            }
        });

        binding.loginPasswordEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changePassword(editable.toString().trim());
            }
        });

        binding.loginSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registrationFragment);
            }
        });

        binding.loginRememberCb.setChecked(prefs.getRemember());
        if(prefs.getRemember()){
            binding.loginLoginEt.setText(prefs.getPrefsLogin());
            binding.loginPasswordEt.setText(prefs.getPrefsPassword());
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });

        subscribe(viewModel);

    }
    private void subscribe(LoginViewModel viewModel) {
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error -> {
            binding.loginAccountErrorTv.setVisibility(View.VISIBLE);
            binding.loginAccountErrorTv.setText(error);
        });
        viewModel.confirmLiveData.observe(getViewLifecycleOwner(), unused -> {
            View view = getView();
            if (view == null) return;
            navigationBarChangeListener.showNavigationBar();
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_lessonsFragment);
        });

        viewModel.teacherLiveData.observe(getViewLifecycleOwner(), state -> {
            prefs.updatePrefs(
                    state.getTeacher().getId(),
                    state.getTeacher().getUsername(),
                    state.getPassword(),
                    state.getTeacher().getName(),
                    state.getTeacher().getSurname(),
                    state.getTeacher().getTelegram_url(),
                    state.getTeacher().getGithub_url(),
                    state.getTeacher().getPhoto_url(),
                    binding.loginRememberCb.isChecked()
            );
        });
        viewModel.loadingLiveData.observe(getViewLifecycleOwner(), loading ->{
            binding.loadingProgressBar.setVisibility(Utils.visibleOrGone(loading));
        });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            prefs = (UpdateSharedPreferences) context;
            navigationBarChangeListener = (NavigationBarChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
