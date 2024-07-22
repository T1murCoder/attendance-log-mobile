package ru.technosopher.attendancelogapp.ui.lessons;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.technosopher.attendancelogapp.databinding.ItemDummyListBinding;
import ru.technosopher.attendancelogapp.domain.entities.ItemGroupEntity;
import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;
import ru.technosopher.attendancelogapp.ui.groups.GroupsListAdapter;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.ViewHolder> {
    private final List<ItemStudentEntity> data = new ArrayList<>();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DummyAdapter.ViewHolder(
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
    public void updateData(List<ItemStudentEntity> newData){
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
        public void bind(ItemStudentEntity item) {
            binding.studentName.setText(item.getFullName());
        }
    }
}
