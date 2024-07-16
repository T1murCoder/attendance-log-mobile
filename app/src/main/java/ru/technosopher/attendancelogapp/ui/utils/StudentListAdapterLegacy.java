package ru.technosopher.attendancelogapp.ui.utils;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import ru.technosopher.attendancelogapp.databinding.ItemGroupsAddStudentsListBinding;

public class StudentListAdapterLegacy extends RecyclerView.Adapter<StudentListAdapterLegacy.ViewHolder>{
    private Consumer<String> onAddStudent;
    private Consumer<String> onDeleteStudent;
    private Consumer<List<String>> onSelectedChange;
    private final List<ItemStudentEntityModel> data = new ArrayList<>();
    public StudentListAdapterLegacy(Consumer<String> onAddStudent, Consumer<String> onDeleteStudent, Consumer<List<String>> onSelectedChange) {
        this.onAddStudent = onAddStudent;
        this.onDeleteStudent = onDeleteStudent;
        this.onSelectedChange = onSelectedChange;
    }
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
                        onAddStudent.accept(data.get(position).getItemStudent().getId());
                    else
                        onDeleteStudent.accept(data.get(position).getItemStudent().getId());
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(item.getId());
                    tmp.add(isChecked ? "t" : "f");
                    onSelectedChange.accept(tmp);
                }
            });
        }
    }
}
