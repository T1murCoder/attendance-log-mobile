package ru.technosopher.attendancelogapp.ui.group_add;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.ItemGroupsAddStudentsListBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private final List<ItemStudentEntityModel> data = new ArrayList<>();
    private Consumer<String> onAddStudent;
    private Consumer<String> onDeleteStudent;
    private Consumer<List<String>> onSelectedChange;

    public StudentListAdapter(Consumer<String> onAddStudent, Consumer<String> onDeleteStudent, Consumer<List<String>> onSelectedChange) {
        this.onAddStudent = onAddStudent;
        this.onDeleteStudent = onDeleteStudent;
        this.onSelectedChange = onSelectedChange;
    }
    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentListAdapter.ViewHolder(
                ItemGroupsAddStudentsListBinding.inflate(LayoutInflater.from(
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
    public void updateData(List<ItemStudentEntityModel> newData) {
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
                        onAddStudent.accept(data.get(position).getItemStudent().getId());
                        //viewModel.addStudent();
                    else
                        onDeleteStudent.accept(data.get(position).getItemStudent().getId());
                        //viewModel.deleteStudent();
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(item.getId());
                    tmp.add(isChecked ? "t" : "f");
                    onSelectedChange.accept(tmp);
                    //viewModel.updateItemCheckedState(, );
                }
            });
        }
    }
}
