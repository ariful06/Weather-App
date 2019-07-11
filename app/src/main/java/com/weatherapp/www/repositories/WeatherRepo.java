package com.weatherapp.www.repositories;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.weatherapp.www.model.CurrentInfo;
import com.weatherapp.www.model.Data;
import com.weatherapp.www.model.Lists;
import com.weatherapp.www.network.ApiClient;
import com.weatherapp.www.network.ApiInterface;
import com.weatherapp.www.util.Constants;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepo {

    private static final String TAG = "WeatherRepo";
    private MutableLiveData<Data> mutableLiveData;
    private Application application;
    private Data dataList;
    private List<Lists> listOfData;
    private CurrentInfo info;

    private MutableLiveData<CurrentInfo> currentInfoMutableLiveData;

    public WeatherRepo(Application application) {
        this.application = application;
    }

    public MutableLiveData<Data> getMutableLiveData() {
        mutableLiveData = new MutableLiveData<>();
        ApiInterface apiService = ApiClient.getApiClient().create(ApiInterface.class);
        Observable<Data> listObservable = apiService.getWeatherInformations(Constants.LATTITUDE,Constants.LONGITUDE,Constants.NUMBER_OF_DATA_TO_RETRIEVE,Constants.API_KEY);
        listObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Data data) {
                        if (data != null) {
                            listOfData = data.getList();
                            mutableLiveData.postValue(data);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getMessage());
                    }
                    @Override
                    public void onComplete() {

                    }
                });

        return mutableLiveData;
    }

    public LiveData<CurrentInfo> getCurrentTemperature(double lat, double lon) {
        currentInfoMutableLiveData = new MutableLiveData<>();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Observable<CurrentInfo> observable = apiInterface.getCurrentTemperature(lat,lon,Constants.API_KEY);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CurrentInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CurrentInfo currentInfo) {
                        if (currentInfo != null) {
                            info = currentInfo;
                            currentInfoMutableLiveData.postValue(info);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return currentInfoMutableLiveData;
    }
}
