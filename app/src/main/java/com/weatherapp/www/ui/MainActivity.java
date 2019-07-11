package com.weatherapp.www.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.weatherapp.www.R;
import com.weatherapp.www.adapter.WeatherListAdapeter;
import com.weatherapp.www.model.Details;
import com.weatherapp.www.model.Lists;
import com.weatherapp.www.util.Constants;
import com.weatherapp.www.viewmodel.MainActivityViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements WeatherListAdapeter.OnItemClickListener {


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    MainActivityViewModel mainActivityViewModel;
    private WeatherListAdapeter adapter;
    LinearLayoutManager linearLayoutManager;


    //GPS
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    private static final String TAG = "Debug";
    private Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        runtimePermission();
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        getAllWeatherReport();
        swipeRefresh.setOnRefreshListener(this::getAllWeatherReport);

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        getLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        flag = displayGpsStatus();
        if (flag) {
            Log.v(TAG, "onClick");
            Toast.makeText(this, "Please move your device and wait to get updat", Toast.LENGTH_SHORT).show();
            locationListener = new MyLocationListener();
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 3000, 10, locationListener);
        } else {
            alertBox("Gps Status!!", "Your GPS is: OFF");
        }

    }


    private void getAllWeatherReport() {
        mainActivityViewModel.getAllWeatherReport().observe(this, data -> {
            swipeRefresh.setRefreshing(false);
            prepareRecyclerView(data.getList());
        });
    }

    private void getCurrentWeatherInfo(double lat, double lon) {
        mainActivityViewModel.getCurrentTemperature(lat, lon).observe(this, current -> {
            Intent intent = new Intent();
            showNotification(this, "Weather App", String.valueOf((int) (current.getMain().getTemp() - 273)), intent);
        });
    }

    private void prepareRecyclerView(List<Lists> blogList) {
        adapter = new WeatherListAdapeter(this, blogList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_weather)
                .setContentTitle(title)
                .setContentText("Current Temperature: " + body + "°");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notificationId, mBuilder.build());
    }

    public static Intent moveToMainActivity(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void onItemClick(int position, Lists list) {
        int minTemp = (int) (list.getMain().getTempMin() - 273);
        int maxTemp = (int) (list.getMain().getTempMax() - 273);
        int currentTemp = (int) (list.getMain().getTemp() - 273);
        String skyStatus = list.getWeather().get(0).getMain();
        Details details = new Details(list.getName(),
                skyStatus,
                list.getMain().getHumidity(),
                list.getWind().getSpeed(),
                maxTemp,
                minTemp,
                currentTemp,
                list.getCoord().getLat(),
                list.getCoord().getLon());
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra(Constants.DETAILS, details);
        startActivity(intent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //todo gps
            } else {
                runtimePermission();
            }
        }
    }

    public boolean runtimePermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


    protected void alertBox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        (dialog, id) -> {
                            Intent myIntent = new Intent(
                                    Settings.ACTION_SETTINGS);
                            startActivity(myIntent);
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> {
                            dialog.cancel();
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            getCurrentWeatherInfo(loc.getLatitude(), loc.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }


}
