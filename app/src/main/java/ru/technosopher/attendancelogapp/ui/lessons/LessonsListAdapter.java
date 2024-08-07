package ru.technosopher.attendancelogapp.ui.lessons;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.getCodeCacheDir;
import static androidx.core.content.ContextCompat.startActivity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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
    private final Consumer<String> onDelete;
    private final Consumer<String> onOpenJournal;
    private final List<LessonEntityModel> data = new ArrayList<>();
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
    public void updateData(List<LessonEntityModel> newData){
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }
    public void updateItemQrCode(QrCodeEntity qrCode){
        if (qrCode.getId().isEmpty() || qrCode.getId() == null) {
            for (int ind = 0; ind < data.size(); ind++){
                if (data.get(ind).getLesson().getId().equals(qrCode.getLessonId())){
                    data.set(ind, data.get(ind));
                    notifyItemChanged(ind);
                    break;
                }
            }
        }
        else{
            for (int ind = 0; ind < data.size(); ind++){
                if (data.get(ind).getLesson().getId().equals(qrCode.getLessonId())){
                    LessonEntity lessonWithQrCOde = data.get(ind).getLesson();
                    lessonWithQrCOde.setActiveQrCode(qrCode);
                    data.set(ind, new LessonEntityModel(lessonWithQrCOde, data.get(ind).isClosed()));
                    notifyItemChanged(ind);
                    break;
                }
            }
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemLessonsListBinding binding;
        public ViewHolder(@NonNull ItemLessonsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LessonEntityModel item) {
            if (item.isClosed()) {
                binding.qrCodeAdditionalBox.setVisibility(View.GONE);
            } else {
                binding.qrCodeAdditionalBox.setVisibility(View.VISIBLE);
            }
            binding.timeTv.setText(DateFormatter.getFullTimeStringFromDate(item.getLesson().getTimeStart(), item.getLesson().getTimeEnd(), "HH:mm"));
            binding.groupName.setText(item.getLesson().getGroupName());
            binding.dateTv.setText(DateFormatter.getDateStringFromDate(item.getLesson().getTimeStart(), "dd MMM yyyy"));
            binding.lessonTitle.setText(item.getLesson().getTheme());

            if (item.getLesson().getActiveQrCode() == null){
                binding.qrCodeImage.setVisibility(View.GONE);
                binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                binding.emptyQrCodeState.setVisibility(View.VISIBLE);
                binding.qrCodeUploadLayout.setVisibility(View.GONE);
            }
            else{
                binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                binding.emptyQrCodeState.setVisibility(View.GONE);
                binding.qrCodeImage.setVisibility(View.VISIBLE);
                binding.qrCodeImage.setImageBitmap(Utils.generateQr(item.getLesson().getActiveQrCode().getId(), 900, 900));
                binding.qrCodeUploadLayout.setVisibility(View.VISIBLE);

            }
            binding.generateQrCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.lessonItemImageCheckingPb.setVisibility(View.VISIBLE);
                    binding.qrCodeImage.setVisibility(View.GONE);
                    onCheckQrCodeIsAlive.accept(item.getLesson().getId());
                }
            });
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isClosed()){
                        //TransitionManager.beginDelayedTransition(binding.qrCodeAdditionalBox, new AutoTransition());
                        //binding.qrCodeAdditionalBox.setVisibility(View.VISIBLE);
                        expand(binding.qrCodeAdditionalBox);
                        if (item.getLesson().getActiveQrCode() == null){
                            binding.qrCodeImage.setVisibility(View.GONE);
                            binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                            binding.emptyQrCodeState.setVisibility(View.VISIBLE);
                        } else{
                            binding.qrCodeImage.setVisibility(View.VISIBLE);
                            binding.qrCodeImage.setImageBitmap(Utils.generateQr(item.getLesson().getActiveQrCode().getId(), 900, 900));
                            binding.lessonItemImageCheckingPb.setVisibility(View.GONE);
                            binding.emptyQrCodeState.setVisibility(View.GONE);
                        }
                        item.setClosed(false);
                    }else{
                        //TransitionManager.beginDelayedTransition(binding.qrCodeAdditionalBox, new AutoTransition());
                        //binding.qrCodeAdditionalBox.setVisibility(View.GONE);
                        collapse(binding.qrCodeAdditionalBox);
                        item.setClosed(true);
                    }
                }
            });
            binding.lessonDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == null) return;
                    onDelete.accept(item.getLesson().getId());
                }
            });
            binding.qrCodeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onOpenJournal.accept(item.getLesson().getId());
                }
            });
            binding.lessonUploadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getLesson().getActiveQrCode() != null){
                        Bitmap bitmap = Utils.generateQr(item.getLesson().getActiveQrCode().getId(), 900,900);

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
    }

    public static void expand(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);

        ValueAnimator animation = ValueAnimator.ofInt(1, targetHeight);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                view.getLayoutParams().height = value;
                view.requestLayout();
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                view.requestLayout();
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animation.setDuration(200);
        animation.start();
    }

    public static void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        ValueAnimator animation = ValueAnimator.ofInt(initialHeight, 0);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                view.getLayoutParams().height = value;
                view.requestLayout();
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        animation.setDuration(200);
        animation.start();
    }
}
