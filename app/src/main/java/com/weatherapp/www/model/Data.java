package com.weatherapp.www.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Data {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cod")
    @Expose
    private int cod;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("list")
    @Expose
    private List<Lists> list = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Lists>  getList() {
        return list;
    }

    public void setList(List<Lists> list) {
        this.list = list;
    }
}
