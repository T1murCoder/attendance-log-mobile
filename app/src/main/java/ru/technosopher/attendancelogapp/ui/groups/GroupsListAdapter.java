package ru.technosopher.attendancelogapp.ui.groups;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.ItemGroupsListBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;

public class GroupsListAdapter extends RecyclerView.Adapter<GroupsListAdapter.ViewHolder> {

    private final Consumer<String> onItemClick;
    private final Consumer<String> deleteGroup;
    private final List<ItemGroupEntity> data = new ArrayList<>();
    public GroupsListAdapter(Consumer<String> onItemClick, Consumer<String> deleteGroup) {
        this.onItemClick = onItemClick;
        this.deleteGroup = deleteGroup;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemGroupsListBinding.inflate(LayoutInflater.from(
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
    public void updateData(List<ItemGroupEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGroupsListBinding binding;
        public ViewHolder(@NonNull ItemGroupsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemGroupEntity item) {
            binding.groupsListItemGroupName.setText(item.getName());
            binding.openGroupBtn.setOnClickListener(v -> {
                onItemClick.accept(item.getId());
            });
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (view == null) return false;
                    deleteGroup.accept(item.getId());
                    return true;
                }
            });
        }
    }
}
