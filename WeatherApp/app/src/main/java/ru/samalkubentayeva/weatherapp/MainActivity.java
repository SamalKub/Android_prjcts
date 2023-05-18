package ru.samalkubentayeva.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT="com.weatherapp.EXTRA_TEXT";

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV, uvindexTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMITION_CODE = 1;
    private String cityName;
    private Button addCityB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRvWeather);
        cityEdt = findViewById(R.id.idEditCity);
        backIV = findViewById(R.id.idIVBlack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);
        uvindexTV = findViewById(R.id.idTVUVindexMain);
        addCityB = findViewById(R.id.idBAddButton);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMITION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        cityName = getCityName(location.getLongitude(), location.getLatitude());
//        getWeatherInfo(cityName);
////
        if (location != null){cityName = getCityName(location.getLongitude(),location.getLatitude());
            getWeatherInfo(cityName);
        } else {
            cityName = "London";
            getWeatherInfo(cityName);
        }

        searchIV.setOnClickListener(v -> {
            String city = cityEdt.getText().toString();
            if (city.isEmpty()){
                Toast.makeText(MainActivity.this, "Please enter city Name", Toast.LENGTH_SHORT).show();
            }else{
                cityNameTV.setText(cityName);
                getWeatherInfo(city);
            }
        });

        addCityB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddCityActivity();
            }
        });

    }
    public void openAddCityActivity() {
        TextView CityNameTV = findViewById(R.id.idTVCityName);
        String textCityName = CityNameTV.getText().toString();
        Intent intent = new Intent(this, AddCityActivity.class);
        intent.putExtra(EXTRA_TEXT, textCityName);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMITION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for(Address adr: addresses){
                if (adr!=null){
                    String city = adr.getLocality();
                    if (city!=null && !city.equals("")){
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "User City Not Found..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityName) {

//        String url = "http://api.weatherapi.com/v1/forecast.json?key=95894ee6856149fabba192042223005&q=Almaty&days=1&aqi=yes&alerts=yes";
        String url = "http://api.weatherapi.com/v1/forecast.json?key=95894ee6856149fabba192042223005&q="+cityName+"&days=1&aqi=yes&alerts=yes";
        cityNameTV.setText(cityName);
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {

        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String temperature = jsonResponse.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature + "°c");
                    int isDay = jsonResponse.getJSONObject("current").getInt("is_day");
                    String condition = jsonResponse.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = jsonResponse.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                    float uvindex = jsonResponse.getJSONObject("current").getInt("uv");
//                    uvindexTV.setText(uvindex);
                    if (uvindex > 0.0 && uvindex < 3.0) {
                        uvindexTV.setText(uvindex + " Low");
                        uvindexTV.setTextColor(Color.parseColor("#3EA72D"));
//                        uvindexTV.setTextColor(Color.parseColor("#66BB55"));
                    }
                    if (uvindex >= 3.0 && uvindex < 6.0) {
                        uvindexTV.setText(uvindex + " Moderate");
                        uvindexTV.setTextColor(Color.parseColor("#FFF300"));
                    }
                    if (uvindex >= 6.0 && uvindex < 8.0) {
                        uvindexTV.setText(uvindex + " High");
                        uvindexTV.setTextColor(Color.parseColor("#F18B00"));
                    }
                    if (uvindex >= 8.0 && uvindex < 11.0) {
                        uvindexTV.setText(uvindex + " Very high");
                        uvindexTV.setTextColor(Color.parseColor("#E53210"));
                    }
                    if (uvindex >= 11.0) {
                        uvindexTV.setText(uvindex + " Extreme");
                        uvindexTV.setTextColor(Color.parseColor("#B567A4"));
                    }
                    conditionTV.setText(condition);
                    if (isDay == 1) {
//                        morning img http set
//                        Picasso.get().load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fru.freepik.com%2Fpremium-photo%2Fblu-sky-wiht-cloud_3150753.htm&psig=AOvVaw0_91me-fGf9cwmvOcGisfC&ust=1654187572887000&source=images&cd=vfe&ved=2ahUKEwinv9SG14z4AhUGvYsKHe_wAz4QjRx6BAgAEAs").into(backIV);
                        backIV.setImageResource(R.drawable.day);
                    } else {
//                        night img path
//                        Picasso.get().load("").into(backIV);
                        backIV.setImageResource(R.drawable.night);
                    }

                    JSONObject forecastObj = jsonResponse.getJSONObject("forecast");
                    JSONObject forecast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecast0.getJSONArray("hour");

                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String uvind = hourObj.getString("uv");
                        weatherRVModalArrayList.add(new WeatherRVModal(time, temper, img, uvind));
                    }
                    weatherRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name..", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
}