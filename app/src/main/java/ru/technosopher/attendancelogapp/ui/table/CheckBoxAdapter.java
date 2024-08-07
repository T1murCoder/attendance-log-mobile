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

import ru.technosopher.attendancelogapp.databinding.ElementCheckBoxBinding;
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
                ElementCheckBoxBinding.inflate(LayoutInflater.from(
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
        private final ElementCheckBoxBinding binding;
        public ViewHolder(@NonNull ElementCheckBoxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        // TODO (Посмотреть почему создаю новый экземпляр класса (переделать по возможности))
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
