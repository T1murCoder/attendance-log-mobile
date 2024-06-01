package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.StudentTableItemBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;

public class StudentAttendancesAdapter extends RecyclerView.Adapter<StudentAttendancesAdapter.ViewHolder> {
    private final List<StudentEntity> data = new ArrayList<>();

    private boolean state = true;
    private final Context context;
    private final Consumer<AttendanceEntity> setAttendanceAndPointsToStudent;
    public StudentAttendancesAdapter(Context context, boolean state, Consumer<AttendanceEntity> setAttendanceAndPointsToStudent) {
        this.context = context;
        this.state = state;
        this.setAttendanceAndPointsToStudent = setAttendanceAndPointsToStudent;
    }

    @NonNull
    @Override
    public StudentAttendancesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentAttendancesAdapter.ViewHolder(
                StudentTableItemBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendancesAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position), state);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<StudentEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateState(boolean newState) {
        this.state = newState;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final StudentTableItemBinding binding;

        public ViewHolder(@NonNull StudentTableItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StudentEntity item, boolean att) {
            binding.tableStudentName.setText(item.getFullName());
            if (att) {
                CheckBoxAdapter adapter = new CheckBoxAdapter(context, this::changeStudent);
                binding.attAndBallsRv.setAdapter(adapter);
                adapter.update(item.getAttendanceEntityList());
            } else {
                PointsAdapter adapter = new PointsAdapter(context, this::changeStudent);
                binding.attAndBallsRv.setAdapter(adapter);
                adapter.update(item.getAttendanceEntityList());
            }
        }
        private void changeStudent(AttendanceEntity attendance){
            setAttendanceAndPointsToStudent.accept(attendance);
        }
    }
}
