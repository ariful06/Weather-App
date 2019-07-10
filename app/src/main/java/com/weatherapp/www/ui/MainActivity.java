package com.weatherapp.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.weatherapp.www.R;
import com.weatherapp.www.adapter.WeatherListAdapeter;
import com.weatherapp.www.model.Data;
import com.weatherapp.www.model.Lists;
import com.weatherapp.www.viewmodel.MainActivityViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    MainActivityViewModel mainActivityViewModel;
    private WeatherListAdapeter adapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        getAllWeatherReport();
        swipeRefresh.setOnRefreshListener(this::getAllWeatherReport);
        linearLayoutManager = new LinearLayoutManager(this);
    }


    private void getAllWeatherReport() {
        mainActivityViewModel.getAllWeatherReport().observe(this, new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                swipeRefresh.setRefreshing(false);
                prepareRecyclerView(data.getList());
            }
        });    }

    private void prepareRecyclerView(List<Lists> blogList) {
        adapter = new WeatherListAdapeter(this, blogList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public static Intent moveToMainActivity(Context context) {
        return new Intent(context, MainActivity.class);
    }
}
