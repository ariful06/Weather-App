package com.weatherapp.www.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Details implements Parcelable {

    private String name;
    private String skyStatus;
    private int humidity;
    private double windSpeed;
    private int maxTemp;
    private int minTemp;
    private int currentTemp;
    private double lat;
    private double lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkyStatus() {
        return skyStatus;
    }

    public void setSkyStatus(String skyStatus) {
        this.skyStatus = skyStatus;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public static Creator<Details> getCREATOR() {
        return CREATOR;
    }

    public Details(String name, String skyStatus, int humidity, double windSpeed, int maxTemp, int minTemp, int currentTemp, double lat, double lon) {
        this.name = name;
        this.skyStatus = skyStatus;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.currentTemp = currentTemp;
        this.lat = lat;
        this.lon = lon;
    }

    protected Details(Parcel in) {
        name = in.readString();
        skyStatus = in.readString();
        humidity = in.readInt();
        windSpeed = in.readDouble();
        maxTemp = in.readInt();
        minTemp = in.readInt();
        currentTemp = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(skyStatus);
        parcel.writeInt(humidity);
        parcel.writeDouble(windSpeed);
        parcel.writeInt(maxTemp);
        parcel.writeInt(minTemp);
        parcel.writeInt(currentTemp);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }
}
