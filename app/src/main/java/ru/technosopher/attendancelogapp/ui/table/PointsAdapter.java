package ru.technosopher.attendancelogapp.ui.table;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.technosopher.attendancelogapp.databinding.PointEtElementBinding;
import ru.technosopher.attendancelogapp.domain.entities.AttendanceEntity;
import ru.technosopher.attendancelogapp.ui.utils.OnChangeText;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.ViewHolder> {

    private final Context context;
    private final List<AttendanceEntity> data = new ArrayList<>();
    public PointsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PointsAdapter.ViewHolder(
                PointEtElementBinding.inflate(LayoutInflater.from(
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

        private final PointEtElementBinding binding;

        public ViewHolder(@NonNull PointEtElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttendanceEntity item) {
            binding.pointEt.setText(String.valueOf(Math.round(Float.valueOf(item.getPoints()))));
            binding.pointEt.addTextChangedListener(new OnChangeText() {
                @Override
                public void afterTextChanged(Editable editable) {
                    super.afterTextChanged(editable);
                    //todo(request to server)
                }
            });
        }
    }
}
