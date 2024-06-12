package ru.technosopher.attendancelogapp.ui.lessons;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.getCodeCacheDir;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;


import ru.technosopher.attendancelogapp.data.source.QrCodeApi;
import ru.technosopher.attendancelogapp.databinding.ItemLessonsListBinding;
import ru.technosopher.attendancelogapp.domain.entities.LessonEntity;
import ru.technosopher.attendancelogapp.domain.entities.QrCodeEntity;
import ru.technosopher.attendancelogapp.ui.MainActivity;
import ru.technosopher.attendancelogapp.ui.utils.DateFormatter;
import ru.technosopher.attendancelogapp.ui.utils.Utils;

public class LessonsListAdapter extends RecyclerView.Adapter<LessonsListAdapter.ViewHolder> {

    private Context context;

    private final Consumer<String> onCheckQrCodeIsAlive;
//    private final Consumer<String> onItemOpen;
//    private final Consumer<Boolean> onItemClose;
    private final Consumer<String> onDelete;
    private final Consumer<String> onOpenJournal;
//    private final Consumer<String> onUpload;
//    private final Consumer<String> onCopyLink;
    private final List<LessonEntity> data = new ArrayList<>();
//    public LessonsListAdapter(Context context, Consumer<String> onItemOpen, Consumer<Boolean> onItemClose, Consumer<String> onDelete, Consumer<String> onOpenJournal, Consumer<String> onUpload, Consumer<String> onCopyLink) {
//        this.context = context;
//        this.onItemOpen = onItemOpen;
//        this.onItemClose = onItemClose;
//        this.onDelete = onDelete;
//        this.onOpenJournal = onOpenJournal;
//        this.onUpload = onUpload;
//        this.onCopyLink = onCopyLink;
//    }

    public LessonsListAdapter(Context context, Consumer<String> onCheckQrCodeIsAlive, Consumer<String> onDelete, Consumer<String> onOpenJournal) {
        this.context = context;
        this.onCheckQrCodeIsAlive = onCheckQrCodeIsAlive;
        this.onDelete = onDelete;
        this.onOpenJournal = onOpenJournal;
    }

    @NonNull
    @Override
    public LessonsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LessonsListAdapter.ViewHolder(
                ItemLessonsListBinding.inflate(LayoutInflater.from(
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

    public void updateItemQrCode(QrCodeEntity qrCode){
        if (qrCode.getId().isEmpty() || qrCode.getId() == null) {
            for (int ind = 0; ind < data.size(); ind++){
                if (data.get(ind).getId().equals(qrCode.getLessonId())){
                    data.set(ind, data.get(ind));
                    notifyItemChanged(ind);
                    break;
                }
            }
        }
        else{
            for (int ind = 0; ind < data.size(); ind++){
                if (data.get(ind).getId().equals(qrCode.getLessonId())){
                    LessonEntity lessonWithQrCOde = data.get(ind);
                    lessonWithQrCOde.setActiveQrCode(qrCode);
                    data.set(ind, lessonWithQrCOde);
                    notifyItemChanged(ind);
                    break;
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemLessonsListBinding binding;
        private Boolean closed = true;
        public ViewHolder(@NonNull ItemLessonsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LessonEntity item) {

            binding.timeTv.setText(DateFormatter.getFullTimeStringFromDate(item.getTimeStart(), item.getTimeEnd(), "HH:mm"));
            binding.groupName.setText(item.getGroupName());
            binding.dateTv.setText(DateFormatter.getDateStringFromDate(item.getTimeStart(), "dd MMM yyyy"));
            binding.lessonTitle.setText(item.getTheme());


            if (item.getActiveQrCode() == null){
                binding.qrCodeImage.setVisibility(View.GONE);
                binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                binding.emptyQrCodeState.setVisibility(View.VISIBLE);
                binding.qrCodeUploadLayout.setVisibility(View.GONE);
                binding.qrCodeCopyLinkLayout.setVisibility(View.GONE);
            }
            else{
                binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                binding.emptyQrCodeState.setVisibility(View.GONE);
                binding.qrCodeImage.setVisibility(View.VISIBLE);
                binding.qrCodeImage.setImageBitmap(Utils.generateQr(item.getActiveQrCode().getId(), 900, 900));
                binding.qrCodeUploadLayout.setVisibility(View.VISIBLE);
                binding.qrCodeCopyLinkLayout.setVisibility(View.VISIBLE);
            }
            binding.generateQrCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.lessonItemImageCheckingPb.setVisibility(View.VISIBLE);
                    binding.qrCodeImage.setVisibility(View.GONE);
                    onCheckQrCodeIsAlive.accept(item.getId());
                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (closed){
                        binding.qrCodeAdditionalBox.setVisibility(View.VISIBLE);
                        if (item.getActiveQrCode() == null){
                            binding.qrCodeImage.setVisibility(View.GONE);
                            binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                            binding.emptyQrCodeState.setVisibility(View.VISIBLE);
                        } else{
                            binding.qrCodeImage.setVisibility(View.VISIBLE);
                            binding.qrCodeImage.setImageBitmap(Utils.generateQr(item.getActiveQrCode().getId(), 900, 900));
                            binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                            binding.emptyQrCodeState.setVisibility(View.GONE);
                        }
                        closed = false;
                    }else{
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
            binding.qrCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOpenJournal.accept(item.getId());
                }
            });
            binding.lessonUploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getActiveQrCode() != null){
                        Bitmap bitmap = Utils.generateQr(item.getActiveQrCode().getId(), 900,900);

                        File imagePath = new File(context.getCacheDir(), "to-share-qr.png");
                        try {
                            FileOutputStream fos = new FileOutputStream(imagePath);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Uri imageUri = FileProvider.getUriForFile(view.getContext(), "ru.technosopher.attendancelogapp.fileprovider", imagePath);

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        shareIntent.setType("image/*");
                        Intent chooser = Intent.createChooser(shareIntent, "Поделиться с помощью");
                        if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(chooser);
                        }
                    }

                }
            });
        }
        public Boolean getIsClosed(){
            return this.closed;
        }
    }
}
