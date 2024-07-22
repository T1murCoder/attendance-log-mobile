package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.ItemDummyListBinding;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;

public class StudentsListAdapterForTable extends RecyclerView.Adapter<StudentsListAdapterForTable.ViewHolder> {
    private final List<StudentEntity> data = new ArrayList<>();
    private final Consumer<String> onStudentDelete;
    public StudentsListAdapterForTable(Consumer<String> onStudentDelete){
        this.onStudentDelete = onStudentDelete;
    }
    @NonNull
    @Override
    public StudentsListAdapterForTable.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemDummyListBinding.inflate(LayoutInflater.from(
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
    public void updateData(List<StudentEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDummyListBinding binding;
        public ViewHolder(@NonNull ItemDummyListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StudentEntity item) {
            binding.studentName.setText(item.getFullName());
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (view == null) return false;
                    onStudentDelete.accept(item.getId());
                    return true;
                }
            });
        }
    }
}
