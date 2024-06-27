package ru.technosopher.attendancelogapp.ui.group_add;


import ru.technosopher.attendancelogapp.domain.entities.ItemStudentEntity;

public class ItemStudentEntityModel {
    private String id;
    private ItemStudentEntity itemStudent;
    private boolean isChecked;

    public ItemStudentEntityModel(String id, ItemStudentEntity itemStudent, boolean isChecked) {
        this.id = id;
        this.itemStudent = itemStudent;
        this.isChecked = isChecked;
    }

    public ItemStudentEntity getItemStudent() {
        return itemStudent;
    }

    public String getId() {
        return id;
    }

    public void setItemStudent(ItemStudentEntity itemStudent) {
        this.itemStudent = itemStudent;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ItemStudentEntityModel{" +
                "id='" + id + '\'' +
                ", itemStudent=" + itemStudent +
                ", isChecked=" + isChecked +
                '}';
    }
}