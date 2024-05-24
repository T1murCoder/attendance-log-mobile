package ru.technosopher.attendancelogapp.ui.lessons;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;

import ru.technosopher.attendancelogapp.databinding.LessonsListItemBinding;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class LessonsListAdapter extends RecyclerView.Adapter<LessonsListAdapter.ViewHolder>{

    private final Consumer<Boolean> onItemOpen;
    private final Consumer<Boolean> onItemClose;
    private final Consumer<String> onDelete;
    private final Consumer<String> onUpload;
    private final Consumer<String> onCopyLink;

    //TODO (add active and inactive to checkboxes)
    private final List<LessonEntity> data = new ArrayList<>();
    public LessonsListAdapter(Consumer<Boolean> onItemOpen, Consumer<Boolean> onItemClose, Consumer<String> onDelete, Consumer<String> onUpload, Consumer<String> onCopyLink) {
        this.onItemOpen = onItemOpen;
        this.onItemClose = onItemClose;
        this.onDelete = onDelete;
        this.onUpload = onUpload;
        this.onCopyLink = onCopyLink;
    }

    @NonNull
    @Override
    public LessonsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LessonsListAdapter.ViewHolder(
                LessonsListItemBinding.inflate(LayoutInflater.from(
                                parent.getContext()),
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsListAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<LessonEntity> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final LessonsListItemBinding binding;
        private Boolean closed = true;
        public ViewHolder(@NonNull LessonsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LessonEntity item) {
            binding.timeTv.setText(DateFormatter.getFullTimeStringFromDate(item.getTimeStart(), item.getTimeEnd(), "HH:mm"));
            binding.groupName.setText(item.getGroupName());
            binding.dateTv.setText(DateFormatter.getDateStringFromDate(item.getDate(), "dd MMM yyyy"));
            binding.lessonTitle.setText(item.getTheme());
            //TODO(start time suffer)
            binding.timeStartTv.setText("smh to do 5 min");
            binding.qrCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (closed){
                        onItemOpen.accept(true);
                        binding.qrCodeAdditionalBox.setVisibility(View.VISIBLE);
                        closed = false;
                    }
                    else{
                        onItemClose.accept(true);
                        binding.qrCodeAdditionalBox.setVisibility(View.GONE);
                        closed = true;
                    }
                }
            });
            binding.lessonDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == null) return;
                    onDelete.accept(item.getId());
                }
            });
        }
        //TODO(Change extract methods. Add field validation)
        private String extractDate(GregorianCalendar date) {
            String year = String.valueOf(date.get(Calendar.YEAR));
            String month = String.valueOf(date.get(Calendar.MONTH));
            String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
            return day + " " + getMonthName(Integer.parseInt(month)) + " " + year;
        }

        private String extractTime(GregorianCalendar timeStart, GregorianCalendar timeEnd){
            String ts = timeStart.get(Calendar.HOUR_OF_DAY) + ":" + timeStart.get(Calendar.MINUTE);
            String te = timeEnd.get(Calendar.HOUR_OF_DAY) + ":" + timeEnd.get(Calendar.MINUTE);
            return ts + " - " + te;
        }

        private String getMonthName (int monthNum){
            switch (monthNum){
                case 0: return "янв";
                case 1: return "фев";
                case 2: return "мар";
                case 3: return "апр";
                case 4: return "мая";
                case 5: return "июн";
                case 6: return "июл";
                case 7: return "авг";
                case 8: return "сен";
                case 9: return "окт";
                case 10: return"ноя";
                case 11: return"дек";
            }
            return "net";
        }
    }
}
