package com.weatherapp.www.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.weatherapp.www.R;
import com.weatherapp.www.adapter.WeatherListAdapeter;
import com.weatherapp.www.model.Data;
import com.weatherapp.www.model.Details;
import com.weatherapp.www.model.Lists;
import com.weatherapp.www.service.LocationService;
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

    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (runtimePermission()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            startService(intent);
        } else {
            runtimePermission();
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            stopService(intent);
        }

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        getAllWeatherReport();
        swipeRefresh.setOnRefreshListener(this::getAllWeatherReport);

    }


    private void getAllWeatherReport() {
        mainActivityViewModel.getAllWeatherReport().observe(this, data -> {
            swipeRefresh.setRefreshing(false);
            prepareRecyclerView(data.getList());
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

                Intent intent = new Intent(getApplicationContext(),LocationService.class);
                startService(intent);
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
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(context, "Location Updated", Toast.LENGTH_SHORT).show();
                }
            };
        }
        registerReceiver(receiver, new IntentFilter(Constants.LOCATION_UPDATE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

}
