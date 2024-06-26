package ru.technosopher.attendancelogapp.ui.student_add;

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

import ru.technosopher.attendancelogapp.databinding.ItemGroupsAddStudentsListBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.ui.group_add.ItemStudentEntityModel;

public class StudentListAdapterLegacy extends RecyclerView.Adapter<StudentListAdapterLegacy.ViewHolder>{

    private StudentAddViewModel viewModel;
    private final List<ItemStudentEntityModel> data = new ArrayList<>();
    public StudentListAdapterLegacy(StudentAddViewModel viewModel) {this.viewModel = viewModel;}

    @NonNull
    @Override
    public StudentListAdapterLegacy.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentListAdapterLegacy.ViewHolder(
                ItemGroupsAddStudentsListBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapterLegacy.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ItemStudentEntityModel> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGroupsAddStudentsListBinding binding;
        public ViewHolder(@NonNull ItemGroupsAddStudentsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemStudentEntityModel item) {
            binding.listItemStudentName.setText(item.getItemStudent().getFullName());
            binding.listItemStudentId.setText(item.getItemStudent().getUsername());
            binding.studentAddCb.setOnCheckedChangeListener(null);
            binding.studentAddCb.setChecked(item.isChecked());
            binding.studentAddCb.setOnCheckedChangeListener((button, isChecked) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    data.get(position).setChecked(isChecked);
                    if (isChecked)
                        viewModel.addStudent(data.get(position).getItemStudent().getId());
                    else
                        viewModel.deleteStudent(data.get(position).getItemStudent().getId());
                    viewModel.updateItemCheckedState(item.getId(), isChecked);
                }
            });
        }
    }
}
