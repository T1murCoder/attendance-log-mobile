package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.CheckBoxElementBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>{
    private final Context context;
    private final List<AttendanceEntity> data = new ArrayList<>();
    private final Consumer<AttendanceEntity> changeStudent;

    public CheckBoxAdapter(Context context, Consumer<AttendanceEntity> changeStudent) {
        this.context = context;
        this.changeStudent = changeStudent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckBoxAdapter.ViewHolder(
                CheckBoxElementBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<AttendanceEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBoxElementBinding binding;
        public ViewHolder(@NonNull CheckBoxElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceEntity item) {
            binding.cbElementCheckbox.setChecked(item.getVisited());
            binding.cbElementCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    changeStudent.accept(new AttendanceEntity(item.getId(), b, item.getStudentId(), item.getLessonId(), item.getLessonTimeStart(), item.getPoints()));
                }
            });
        }
    }
}
