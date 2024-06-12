package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.ElementPointEtBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private final Context context;
    private final List<AttendanceEntity> data = new ArrayList<>();

    private final Consumer<AttendanceEntity> changeStudent;

    public PointsAdapter(Context context, Consumer<AttendanceEntity> changeStudent) {
        this.context = context;
        this.changeStudent = changeStudent;
    }

    @NonNull
    @Override
    public PointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PointsAdapter.ViewHolder(
                ElementPointEtBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull PointsAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(List<AttendanceEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ElementPointEtBinding binding;

        public ViewHolder(@NonNull ElementPointEtBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceEntity item) {
            Integer point = Math.round(Float.valueOf(item.getPoints()));
            if (point == 0) binding.pointEt.setHint(String.valueOf(point));
            else binding.pointEt.setText(String.valueOf(point));

            binding.pointEt.addTextChangedListener(new OnChangeText() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    changeStudent.accept(new AttendanceEntity(item.getId(), item.getVisited(), item.getStudentId(), item.getLessonId(), item.getLessonTimeStart(), editable.toString()));
                }
            });
        }
    }
}
