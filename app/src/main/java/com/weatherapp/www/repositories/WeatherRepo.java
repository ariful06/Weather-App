package com.weatherapp.www.repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

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

    private MutableLiveData<Data> mutableLiveData;
    private Application application;
    private Data dataList;
    private List<Lists> listOfData;

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

                        Toast.makeText(application, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {

                        Toast.makeText(application, "", Toast.LENGTH_SHORT).show();
                    }
                });

        return mutableLiveData;
    }
}
