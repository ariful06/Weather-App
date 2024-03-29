package com.weatherapp.www.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.weatherapp.www.R;
import com.weatherapp.www.model.Lists;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherListAdapeter extends RecyclerView.Adapter<WeatherListAdapeter.WeatherViewHolder> {

    List<Lists> dataList;
    private OnItemClickListener onItemClickListener;

    public WeatherListAdapeter(Context context, List<Lists> dataList) {
        this.dataList = dataList;
        onItemClickListener = (OnItemClickListener) context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_list, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        int temp = (int) dataList.get(position).getMain().getTemp();
        String city = dataList.get(position).getName();
        holder.tvCityName.setText(city);
        holder.tvTemperature.setText(((temp - 273) +"°" ));
        holder.tvWeatherReport.setText((dataList.get(position).getWeather().get(0).getMain()));
        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(position,dataList.get(position)));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_city_name)
        TextView tvCityName;
        @BindView(R.id.text_view_temperature)
        TextView tvTemperature;
        @BindView(R.id.text_view_weather_cloud)
        TextView tvWeatherReport;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position,Lists list);
    }
}
