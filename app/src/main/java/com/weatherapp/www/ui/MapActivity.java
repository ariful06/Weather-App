package com.weatherapp.www.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weatherapp.www.R;
import com.weatherapp.www.model.Details;
import com.weatherapp.www.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.text_view_city_name)
    TextView cityName;

    @BindView(R.id.text_view_sky_status)
    TextView skyStatus;

    @BindView(R.id.text_view_humidity)
    TextView humidity;

    @BindView(R.id.text_view_wind_speed)
    TextView windSpeed;

    @BindView(R.id.text_view_max_temp)
    TextView maxTemp;

    @BindView(R.id.text_view_min_temp)
    TextView minTemp;

    @BindView(R.id.text_view_current_temp)
    TextView currentTemp;

    @BindView(R.id.image_view)
    ImageView imageView;

    GoogleMap map;
    Details details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        if (getIntent().hasExtra(Constants.DETAILS)) {
            details = getIntent().getParcelableExtra(Constants.DETAILS);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        initView();
    }

    private void initView() {
        cityName.setText(details.getName());
        skyStatus.setText(details.getSkyStatus());
        humidity.setText(("Humidity: " + details.getHumidity()));
        windSpeed.setText(("Wind Speed: " + details.getWindSpeed()));
        maxTemp.setText(("Max Temp.: " + details.getMaxTemp()));
        minTemp.setText(("Min Temp.: " + details.getMinTemp()));
        currentTemp.setText((details.getCurrentTemp() + "°"));
        if (details.getSkyStatus().equals(Constants.RAIN)) {
            Glide.with(this).load(R.drawable.rain).into(imageView);
        } else if (details.getSkyStatus().equals(Constants.CLOUD)) {
            Glide.with(this).load(R.drawable.clouds).into(imageView);
        } else {
            Glide.with(this).load(R.drawable.clear_sky).into(imageView);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng cityLocation = new LatLng(details.getLat(), details.getLon());
        map.addMarker(new MarkerOptions().position(cityLocation).title(details.getName()));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 12));
    }
}
