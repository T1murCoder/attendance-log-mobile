package ru.technosopher.attendancelogapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import ru.technosopher.attendancelogapp.R;
import ru.technosopher.attendancelogapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationBarChangeListener{
    private NavController navController;
    private ChipNavigationBar navigationBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBar = findViewById(R.id.bottom_navigation_bar);
        navigationBar.setOnItemSelectedListener(destinationFragment -> {
            fragmentNavigation(0, destinationFragment);
        });
    }

    @Override
    public void hideNavigationBar() {
        if (navigationBar != null) navigationBar.setVisibility(View.GONE);
    }

    @Override
    public void showNavigationBar() {
        if (navigationBar != null) navigationBar.setVisibility(View.VISIBLE);
    }

    private void fragmentNavigation(int currentFragment, int destinationFragment) {
        navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);

        if (destinationFragment == R.id.lessons) {
            navController.navigate(R.id.lessonsFragment);
        }
        if (destinationFragment == R.id.groups) {
            navController.navigate(R.id.groupsFragment);
        }
        if (destinationFragment == R.id.profile) {
            navController.navigate(R.id.profileFragment);
        }
    }

}