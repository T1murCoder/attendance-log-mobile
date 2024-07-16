package ru.technosopher.attendancelogapp.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentProfileBinding;
import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;
import ru.technosopher.attendancelogapp.ui.utils.UpdateSharedPreferences;


public class ProfileFragment extends Fragment {
    private final String TAG = "ProfileFragment";
    private NavigationBarChangeListener navigationBarChangeListener;
    private UpdateSharedPreferences prefs;
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private Uri userAvatarUri;
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private ActivityResultLauncher<CropImageContractOptions> cropImageActivity = registerForActivityResult(new CropImageContract(),
            result -> {
                if (result.isSuccessful()) {
                    userAvatarUri = result.getUriContent();
                    Glide.with(this).load(userAvatarUri).into(binding.profileAvatarIv);
                    viewModel.uploadAvatar(prefs.getPrefsId(), prefs.getPrefsLogin(), userAvatarUri);
                } else {
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            });

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

        binding.profileNewImageFab.setOnClickListener(v -> {
            startCrop();
        });

        binding.profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.logout();
            }
        });
        binding.profileNameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileNameEt.isFocusable()) {
                    binding.profileNameEt.setFocusable(true);
                    binding.profileNameEt.setFocusableInTouchMode(true);
                    binding.profileNameEt.setEnabled(true);
                } else {
                    binding.profileNameEt.setFocusable(false);
                    binding.profileNameEt.setFocusableInTouchMode(false);
                    binding.profileNameEt.setEnabled(false);
                }

            }
        });
        binding.profileSurnameEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileSurnameEt.isFocusable()) {
                    binding.profileSurnameEt.setFocusable(true);
                    binding.profileSurnameEt.setFocusableInTouchMode(true);
                    binding.profileSurnameEt.setEnabled(true);
                } else {
                    binding.profileSurnameEt.setFocusable(false);
                    binding.profileSurnameEt.setFocusableInTouchMode(false);
                    binding.profileSurnameEt.setEnabled(false);
                }
            }
        });
        binding.profileTelegramEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileTelegramEt.isFocusable()) {
                    binding.profileTelegramEt.setFocusable(true);
                    binding.profileTelegramEt.setFocusableInTouchMode(true);
                    binding.profileTelegramEt.setEnabled(true);
                } else {
                    binding.profileTelegramEt.setFocusable(false);
                    binding.profileTelegramEt.setFocusableInTouchMode(false);
                    binding.profileTelegramEt.setEnabled(false);
                }
            }
        });
        binding.profileGithubEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.profileGithubEt.isFocusable()) {
                    binding.profileGithubEt.setFocusable(true);
                    binding.profileGithubEt.setFocusableInTouchMode(true);
                    binding.profileGithubEt.setEnabled(true);
                } else {
                    binding.profileGithubEt.setFocusable(false);
                    binding.profileGithubEt.setFocusableInTouchMode(false);
                    binding.profileGithubEt.setEnabled(false);
                }
            }
        });

        binding.profileNameEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeName(editable.toString().trim());
            }
        });
        binding.profileSurnameEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeSurname(editable.toString().trim());
            }
        });

        binding.profileTelegramEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeTelegram(editable.toString());
            }
        });
        binding.profileGithubEt.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                viewModel.changeGithub(editable.toString());
            }
        });

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.updateProfile(prefs.getPrefsId(), prefs.getPrefsLogin());

                binding.profileGithubEt.setFocusable(false);
                binding.profileGithubEt.setFocusableInTouchMode(false);
                binding.profileGithubEt.setEnabled(false);

                binding.profileSurnameEt.setFocusable(false);
                binding.profileSurnameEt.setFocusableInTouchMode(false);
                binding.profileSurnameEt.setEnabled(false);

                binding.profileNameEt.setFocusable(false);
                binding.profileNameEt.setFocusableInTouchMode(false);
                binding.profileNameEt.setEnabled(false);

                binding.profileTelegramEt.setFocusable(false);
                binding.profileTelegramEt.setFocusableInTouchMode(false);
                binding.profileTelegramEt.setEnabled(false);
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
    private void startCrop() {
        CropImageOptions options = new CropImageOptions();
        options.imageSourceIncludeCamera = false;
        options.imageSourceIncludeGallery = true;
        options.aspectRatioX = 1;
        options.aspectRatioY = 1;
        options.cropShape = CropImageView.CropShape.RECTANGLE;
        options.fixAspectRatio = true;
        options.showCropOverlay = true;
        options.outputCompressFormat = Bitmap.CompressFormat.PNG;

        CropImageContractOptions cropOptions = new CropImageContractOptions(null, options);

        cropImageActivity.launch(cropOptions);
    }
    private void loadAvatar(String imageUrl) {
        StorageReference imageRef = storageRef.child(imageUrl);
        imageRef.getDownloadUrl().addOnCompleteListener(task -> {
            try {
                if (task != null && task.getResult() != null && task.isSuccessful()) {
                    Glide.with(requireContext()).load(task.getResult()).into(binding.profileAvatarIv);
                }
                Log.d(TAG, "loadAvatar: " + task.isSuccessful());
            } catch (RuntimeExecutionException e) {
                Log.d(TAG, "loadAvatar: " + false);
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "loadAvatar: " + false);
        }).addOnCanceledListener(() -> {
            Log.d(TAG, "loadAvatar: " + false);
        });
    }
    private void subscribe(ProfileViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (Boolean.TRUE.equals(state.getLoading())) {
                binding.profileLoading.setVisibility(View.VISIBLE);
                binding.profileContent.setVisibility(View.GONE);
                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            } else {
                binding.swipe.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);
                binding.profileLoading.setVisibility(View.GONE);
                binding.profileContent.setVisibility(View.VISIBLE);

                TeacherEntity teacher = state.getTeacher();
                if (teacher != null) {
                    prefs.profileUpdate(
                            state.getTeacher().getName(),
                            state.getTeacher().getSurname(),
                            state.getTeacher().getTelegram_url(),
                            state.getTeacher().getGithub_url(),
                            state.getTeacher().getPhoto_url()
                    );
                    binding.profileLoginTv.setText(teacher.getUsername());
                    binding.profileNameEt.setText(teacher.getName());
                    binding.profileSurnameEt.setText(teacher.getSurname());
                    binding.profileTelegramEt.setText(teacher.getTelegram_url() != null ? teacher.getTelegram_url() : "Provide your telegram");
                    binding.profileGithubEt.setText(teacher.getGithub_url() != null ? teacher.getGithub_url() : "Provide your github");
                    if (teacher.getPhoto_url() != null) {
                        loadAvatar(teacher.getPhoto_url());
                    }
                } else {
                    Toast.makeText(getContext(), state.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
            }
        });
        viewModel.logoutLiveData.observe(getViewLifecycleOwner(), unused -> {
            prefs.clearAll();
            View view = getView();
            if (view == null) return;
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
        });
    }
}