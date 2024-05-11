package ru.technosopher.attendancelogapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChipNavigationBar navigationBar = findViewById(R.id.bottom_navigation_bar);
        navigationBar.setOnItemSelectedListener(i -> {
            int a = navigationBar.getSelectedItemId(); // TODO (check current item selected)
            navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);

            if (i == R.id.lessons) {
                navController.navigate(R.id.lessonsFragment);
            }
            if (i == R.id.groups) {
                navController.navigate(R.id.groupsFragment);
            }
            if (i == R.id.profile) {
                navController.navigate(R.id.profileFragment);
            }
        });

    }
}