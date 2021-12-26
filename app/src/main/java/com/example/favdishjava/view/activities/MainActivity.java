package com.example.favdishjava.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.favdishjava.R;
import com.example.favdishjava.databinding.ActivityMainBinding;
import com.example.favdishjava.model.notification.NotifyWorker;
import com.example.favdishjava.utils.Constants;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController mNavController;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_all_dishes, R.id.navigation_favorite_dishes, R.id.navigation_random_dish)
                .build();

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, mNavController);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(Constants.NOTIFICATION_ID)) {
            int notificationId = getIntent().getIntExtra(Constants.NOTIFICATION_ID, 0);
            Log.i("Notification Id", "$notificationId");

            // The Random Dish Fragment is selected when user is redirect in the app via Notification.
            binding.navView.setSelectedItemId(R.id.navigation_random_dish);
        }
        startWork();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }

    public void hideBottomNavigationView() {
        binding.navView.clearAnimation();
        binding.navView.animate().translationY(binding.navView.getHeight()).setDuration(300);
        binding.navView.setVisibility(View.GONE);
    }

    public void showBottomNavigationView() {
        binding.navView.clearAnimation();
        binding.navView.animate().translationY(0F).setDuration(300);
        binding.navView.setVisibility(View.VISIBLE);
    }

    private Constraints CreateConstraints() {
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)  // if connected to WIFI
                .setRequiresCharging(false)
                .setRequiresBatteryNotLow(true)                 // if the battery is not low
                .build();
    }

    private PeriodicWorkRequest createWorkRequest() {
        return new PeriodicWorkRequest.Builder(NotifyWorker.class, 15, TimeUnit.SECONDS).setConstraints(CreateConstraints()).build();
    }

    private void startWork() {
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork("FavDish Notify Work", ExistingPeriodicWorkPolicy.REPLACE, createWorkRequest());
    }

}