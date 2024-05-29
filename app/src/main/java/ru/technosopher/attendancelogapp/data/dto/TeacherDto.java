package ru.technosopher.attendancelogapp.data.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;


public class TeacherDto {

    @Nullable
    @SerializedName("id")
    public String id;
    @Nullable
    @SerializedName("name")
    public String name;
    @Nullable
    @SerializedName("surname")
    public String surname;
    @Nullable
    @SerializedName("username")
    public String username;
    @Nullable
    @SerializedName("telegramUrl")
    public String telegram_url;
    @Nullable
    @SerializedName("githubUrl")
    public String github_url;
    @Nullable
    @SerializedName("photoUrl")
    public String photo_url;

}
