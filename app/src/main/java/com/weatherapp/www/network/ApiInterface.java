package com.weatherapp.www.network;

import com.weatherapp.www.model.Data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("data/2.5/find")
    Observable<Data> getWeatherInformations(@Query("lat") double lat,
                                            @Query("lon") double lon,
                                            @Query("cnt") int numberOfData,
                                            @Query("appid") String appID
    );

}

