package com.mohitingale.agroguide.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mohitingale.agroguide.R;

import org.json.JSONArray;
import org.json.JSONObject;
import com.bumptech.glide.Glide;
import java.util.Calendar;


public class HomeFragment extends Fragment {
    private final String API_KEY = "35f4e31839a0a4d553da6af75192e070";
    TextView tvGreeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        TextView tvTemperature = view.findViewById(R.id.tvTemperature);
        TextView tvHumidity = view.findViewById(R.id.tvHumidity);
        TextView tvWindSpeed = view.findViewById(R.id.tvWindSpeed);
        TextView tvWeather = view.findViewById(R.id.tvWeather);
        tvGreeting = view.findViewById(R.id.tvGreeting);

        ImageView imgWeather = view.findViewById(R.id.imgWeather);
        getWeather("Pune", tvTemperature, tvHumidity, tvWindSpeed, tvWeather, imgWeather);
        setGreeting();
        return view;
    }
    private void setGreeting() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning, Mohit 👋";
        }
        else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon, Mohit ☀️";
        }
        else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening, Mohit 🌇";
        }
        else {
            greeting = "Good Night, Mohit 🌙";
        }
        tvGreeting.setText(greeting);
    }
    private void getWeather(String cityName, TextView tvTemperature, TextView tvHumidity, TextView tvWindSpeed, TextView tvWeather, ImageView imgWeather) {
        String city = cityName;
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&units=metric&appid=" + API_KEY;

            RequestQueue queue = Volley.newRequestQueue(requireContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                        try {

                            String temp = response.getJSONObject("main").getString("temp");
                            String humidity = response.getJSONObject("main").getString("humidity");
                            String wind = response.getJSONObject("wind").getString("speed");

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String condition = weather.getString("main");
                            String icon = weather.getString("icon");
                            tvTemperature.setText(temp + "°C");
                            tvHumidity.setText(humidity + "%");
                            tvWindSpeed.setText(wind + " km/h");
                            tvWeather.setText(condition);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(requireContext(),
                            "Weather Not Found",
                            Toast.LENGTH_SHORT).show());

            queue.add(request);
        }
    }