package com.weatherapp.www.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.weatherapp.www.model.Data;
import com.weatherapp.www.repositories.WeatherRepo;

public class MainActivityViewModel  extends AndroidViewModel {

    private WeatherRepo weatherRepo;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        weatherRepo = new WeatherRepo(application);
    }
    public LiveData<Data> getAllWeatherReport() {
        return weatherRepo.getMutableLiveData();
    }

}
