package ru.technosopher.attendancelogapp.ui.group_manage;

import static ru.technosopher.attendancelogapp.ui.utils.Utils.setClipboard;

import android.content.Context;
import android.content.Intent;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.FragmentGroupManageBinding;
import ru.technosopher.attendancelogapp.ui.table.TableFragment;
import ru.technosopher.attendancelogapp.ui.table.TableViewModel;
import ru.technosopher.attendancelogapp.ui.utils.NavigationBarChangeListener;

public class GroupManageFragment extends Fragment {

    private final static String TAG = "GroupManageFragment";

    private final static String KEY_ID = "GROUP_MANAGE";

    private FragmentGroupManageBinding binding;

    private GroupManageViewModel viewModel;

    private NavigationBarChangeListener navigationBarChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationBarChangeListener.hideNavigationBar();
        binding = FragmentGroupManageBinding.bind(view);

        String groupId = getArguments() != null ? getArguments().getString(KEY_ID) : "-1";

        viewModel = new ViewModelProvider(this).get(GroupManageViewModel.class);
        RequestsAdapter adapter = new RequestsAdapter(this::onAccept, this::onDecline);

        binding.requestRecyclerView.setAdapter(adapter);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                System.out.println(groupId);
                Navigation.findNavController(view).navigate(R.id.action_groupManageFragment_to_tableFragment, TableFragment.getBundle(groupId));
            }
        });
        binding.backWithoutLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == null) return;
                Navigation.findNavController(view).navigate(R.id.action_groupManageFragment_to_tableFragment, TableFragment.getBundle(groupId));
            }
        });
        binding.groupUid.setOnLongClickListener(v -> {
            setClipboard(binding.groupUid.getText().toString(), requireContext());
            return true;
        });
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, binding.groupUid.getText());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Поделиться"));
            }
        });
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.update();
            }
        });
        subscribe(viewModel, adapter);
        viewModel.saveId(groupId);
        viewModel.update();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
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

    private void subscribe(GroupManageViewModel viewModel, RequestsAdapter adapter){
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            if (state.getLoading()){
                binding.errorTv.setVisibility(View.GONE);
                binding.content.setVisibility(View.GONE);
                binding.backWithoutLoading.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);

                binding.swipe.setEnabled(false);
                binding.swipe.setVisibility(View.GONE);
            }
            else{
                binding.swipe.setVisibility(View.VISIBLE);
                binding.swipe.setEnabled(true);
                binding.swipe.setRefreshing(false);

                if (state.getSuccess()){
                    System.out.println(state.getRequests());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.backWithoutLoading.setVisibility(View.GONE);
                    binding.errorTv.setVisibility(View.GONE);
                    binding.content.setVisibility(View.VISIBLE);
                    binding.requestRecyclerView.setVisibility(View.VISIBLE);

                    binding.groupUid.setText(state.getJoinCode());

                    if (state.getRequests().isEmpty()){
                        binding.errorTv.setVisibility(View.VISIBLE);
                        binding.errorTv.setText("У вас нет заявок на вступление в группу");
                    }
                    adapter.updateData(state.getRequests());

                }
            }
        });
        viewModel.interactionLiveData.observe(getViewLifecycleOwner(), msg -> {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            viewModel.update();
        });
    }
    private void onAccept(@NonNull String id){
        viewModel.acceptStudent(id);
    }

    private void onDecline(@NonNull String id){
        viewModel.declineStudent(id);
    }

}
