package ru.technosopher.attendancelogapp.ui;

import java.util.Map;

import ru.technosopher.attendancelogapp.domain.entities.TeacherEntity;

public interface UpdateSharedPreferences {

    public void updatePrefs(
            String id,
            String login,
            String password,
            String name,
            String surname,
            String telegram,
            String github,
            String photo,
            Boolean remember
    );
    public void clearAll();
    public Map<String, ?> getPrefs();
    public String getPrefsId();
    public String getPrefsLogin();
    public String getPrefsPassword();
    public String getPrefsTelegram();
    public String getPrefsGithub();
    public String getPrefsPhotoUrl();
    public String getPrefsName();
    public String getPrefsSurname();
    public Boolean getRemember();
    public void updateId(String id);
    public void updateLogin(String id);
    public void updatePassword(String id);
    public void updateTelegram(String telegram);
    public void updateGithub(String github);
    public void updatePhoto(String photo);
    public void updateName(String photo);
    public void updateSurname(String photo);
    public void updateRemember(Boolean b);
}
