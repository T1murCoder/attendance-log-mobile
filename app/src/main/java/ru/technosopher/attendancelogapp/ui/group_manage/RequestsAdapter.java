package ru.technosopher.attendancelogapp.ui.group_manage;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.ItemRequestsToGroupsListBinding;
import ru.technosopher.attendancelogapp.domain.entities.RequestEntity;


public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder>{
    private final Consumer<String> onApplyClick;
    private final Consumer<String> onDenyClick;
    private final List<RequestEntity> data = new ArrayList<>();

    public RequestsAdapter(Consumer<String> onApplyClick, Consumer<String> onDenyClick){
        this.onApplyClick = onApplyClick;
        this.onDenyClick = onDenyClick;
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemRequestsToGroupsListBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<RequestEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRequestsToGroupsListBinding binding;

        public ViewHolder(@NonNull ItemRequestsToGroupsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RequestEntity item) {
            binding.listItemStudentName.setText(item.getStudent().getFullName());
            binding.listItemStudentId.setText(item.getStudent().getUsername());
            binding.applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onApplyClick.accept(item.getId());
                }
            });
            binding.denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDenyClick.accept(item.getId());
                }
            });
        }
    }
}
