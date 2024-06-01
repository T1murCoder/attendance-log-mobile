package ru.technosopher.attendancelogapp.ui.group_add;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.GroupsAddStudentsListItemBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder>{
    private final Consumer<String> onItemSelected;
    private final Consumer<String> onItemUnselected;
    private final List<ItemStudentEntity> data = new ArrayList<>();
    public StudentListAdapter(Consumer<String> onItemSelected, Consumer<String> onItemUnselected) {
        this.onItemSelected = onItemSelected;
        this.onItemUnselected = onItemUnselected;
    }

    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentListAdapter.ViewHolder(
                GroupsAddStudentsListItemBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ItemStudentEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final GroupsAddStudentsListItemBinding binding;
        public ViewHolder(@NonNull GroupsAddStudentsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemStudentEntity item) {
            binding.listItemStudentName.setText(item.getFullName());
            binding.listItemStudentId.setText(item.getStringId());
            binding.studentAddCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) onItemSelected.accept(item.getId());
                    else onItemUnselected.accept(item.getId());
                }
            });
        }
    }
}
