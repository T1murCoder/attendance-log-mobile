package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.ItemStudentTableBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.domain.entities.StudentEntity;

public class StudentAttendancesAdapter extends RecyclerView.Adapter<StudentAttendancesAdapter.ViewHolder> {
    private final List<StudentEntity> data = new ArrayList<>();
    private boolean state = true;
    private final Context context;
    private final Consumer<AttendanceEntity> setAttendanceAndPointsToStudent;
    private final Consumer<String> onStudentDelete;
    public StudentAttendancesAdapter(Context context, boolean state, Consumer<AttendanceEntity> setAttendanceAndPointsToStudent, Consumer<String> onStudentDelete) {
        this.context = context;
        this.state = state;
        this.setAttendanceAndPointsToStudent = setAttendanceAndPointsToStudent;
        this.onStudentDelete = onStudentDelete;
    }
    @NonNull
    @Override
    public StudentAttendancesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentAttendancesAdapter.ViewHolder(
                ItemStudentTableBinding.inflate(LayoutInflater.from(
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
        private final ItemStudentTableBinding binding;

        public ViewHolder(@NonNull ItemStudentTableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StudentEntity item, boolean att) {
            binding.tableStudentName.setText(item.getFullName());
            binding.tvPoints.setText(item.getPoints());
            if (att) {
                CheckBoxAdapter adapter = new CheckBoxAdapter(context, this::changeStudent);
                binding.attAndBallsRv.setAdapter(adapter);
                adapter.update(item.getAttendanceEntityList());
            } else {
                PointsAdapter adapter = new PointsAdapter(context, this::changeStudent);
                binding.attAndBallsRv.setAdapter(adapter);
                adapter.update(item.getAttendanceEntityList());
            }
            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (view == null) return false;
                    onStudentDelete.accept(item.getId());
                    return true;
                }
            });
        }
        private void changeStudent(AttendanceEntity attendance){
            setAttendanceAndPointsToStudent.accept(attendance);
        }
    }
}
