package com.mohitingale.agroguide.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.mohitingale.agroguide.HomeFragmentActivity.AIRecommendationActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.BuyInputsActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.ChatBotActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.CropAdvisoryActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.DiseaseDetectionActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.FertilizerActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.FertilizerGuideActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.IrrigationActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.IrrigationAssistantActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.LearningCenterActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.MarketPriceActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.NewsActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.PestActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.RecentActivitiesActivity;
import com.mohitingale.agroguide.HomeFragmentActivity.ScheduleActivity;
import com.mohitingale.agroguide.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;


public class HomeFragment extends Fragment {
    private final String API_KEY = "35f4e31839a0a4d553da6af75192e070";
    private TextView tvGreeting, tvFarmOverview, tvAIRecommendation, tvViewDetails;
    private ImageView imgRecommendation;
    private MaterialCardView cardCropAdvisory, cardDiseaseDetection, cardIrrigationAssistant, cardFertilizerGuide, cardAIWeather;
    private MaterialCardView cardAskAI, cardMarket, cardBuy, cardNews, cardLearningCentre;
    private TextView tvHome_ra_viewAll, tvLearningCentreViewAll;
    private LinearLayout home_ra_1, home_ra_2, home_ra_3, home_ra_4;
    private TextView tvTemperature, tvHumidity, tvWindSpeed, tvWeather;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        tvTemperature = view.findViewById(R.id.tvTemperature);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvWindSpeed = view.findViewById(R.id.tvWindSpeed);
        tvWeather = view.findViewById(R.id.tvWeather);
        tvGreeting = view.findViewById(R.id.tvGreeting);

        tvFarmOverview = view.findViewById(R.id.tvFarmOverview);
        tvAIRecommendation = view.findViewById(R.id.tvAIRecommendation);
        tvViewDetails = view.findViewById(R.id.tvViewDetails);
        imgRecommendation = view.findViewById(R.id.imgRecommendation);
        cardAIWeather = view.findViewById(R.id.cardAIWeather);

        cardCropAdvisory = view.findViewById(R.id.cardCropAdvisory);
        cardDiseaseDetection = view.findViewById(R.id.cardDiseaseDetection);
        cardIrrigationAssistant = view.findViewById(R.id.cardIrrigationAssistant);
        cardFertilizerGuide = view.findViewById(R.id.cardFertilizerGuide);

        cardAskAI = view.findViewById(R.id.cardAskAI);
        cardMarket = view.findViewById(R.id.cardMarket);
        cardBuy = view.findViewById(R.id.cardBuy);
        cardNews = view.findViewById(R.id.cardnews);

        tvHome_ra_viewAll = view.findViewById(R.id.tvHome_ra_viewAll);
        home_ra_1 = view.findViewById(R.id.home_ra_1);
        home_ra_2 = view.findViewById(R.id.home_ra_2);
        home_ra_3 = view.findViewById(R.id.home_ra_3);
        home_ra_4 = view.findViewById(R.id.home_ra_4);

        cardLearningCentre = view.findViewById(R.id.cardLearningCentre);
        tvLearningCentreViewAll = view.findViewById(R.id.tvLearningCentreViewAll);

        setClickListeners();

        // Initial Setup
        getWeather("Amravati");
        setGreeting();
    }

    private void setClickListeners() {
        if (cardLearningCentre != null) {
            cardLearningCentre.setOnClickListener(v -> startActivity(new Intent(requireContext(), LearningCenterActivity.class)));
        }
        if (tvLearningCentreViewAll != null) {
            tvLearningCentreViewAll.setOnClickListener(v -> startActivity(new Intent(requireContext(), LearningCenterActivity.class)));
        }
        if (tvHome_ra_viewAll != null) {
            tvHome_ra_viewAll.setOnClickListener(v -> startActivity(new Intent(requireContext(), RecentActivitiesActivity.class)));
        }
        if (home_ra_1 != null) {
            home_ra_1.setOnClickListener(v -> startActivity(new Intent(requireContext(), IrrigationActivity.class)));
        }
        if (home_ra_2 != null) {
            home_ra_2.setOnClickListener(v -> startActivity(new Intent(requireContext(), FertilizerActivity.class)));
        }
        if (home_ra_3 != null) {
            home_ra_3.setOnClickListener(v -> startActivity(new Intent(requireContext(), PestActivity.class)));
        }
        if (home_ra_4 != null) {
            home_ra_4.setOnClickListener(v -> startActivity(new Intent(requireContext(), ScheduleActivity.class)));
        }
        if (cardAskAI != null) {
            cardAskAI.setOnClickListener(v -> startActivity(new Intent(requireContext(), ChatBotActivity.class)));
        }
        if (cardMarket != null) {
            cardMarket.setOnClickListener(v -> startActivity(new Intent(requireContext(), MarketPriceActivity.class)));
        }
        if (cardBuy != null) {
            cardBuy.setOnClickListener(v -> startActivity(new Intent(requireContext(), BuyInputsActivity.class)));
        }
        if (cardNews != null) {
            cardNews.setOnClickListener(v -> startActivity(new Intent(requireContext(), NewsActivity.class)));
        }
        if (imgRecommendation != null) {
            imgRecommendation.setOnClickListener(v -> {
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.dialog_image);
                ImageView image = dialog.findViewById(R.id.imagePreview);
                if (image != null) image.setImageDrawable(imgRecommendation.getDrawable());
                dialog.show();
            });
        }
        if (tvViewDetails != null) {
            tvViewDetails.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, new RecommendationDetailsFragment()).commit());
        }
        if (tvAIRecommendation != null) {
            tvAIRecommendation.setOnClickListener(v -> startActivity(new Intent(requireContext(), AIRecommendationActivity.class)));
        }
        if (cardAIWeather != null) {
            cardAIWeather.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, new WeatherDetailsFragment()).commit());
        }
        if (cardCropAdvisory != null) {
            cardCropAdvisory.setOnClickListener(v -> startActivity(new Intent(requireContext(), CropAdvisoryActivity.class)));
        }
        if (cardDiseaseDetection != null) {
            cardDiseaseDetection.setOnClickListener(v -> startActivity(new Intent(requireContext(), DiseaseDetectionActivity.class)));
        }
        if (cardIrrigationAssistant != null) {
            cardIrrigationAssistant.setOnClickListener(v -> startActivity(new Intent(requireContext(), IrrigationAssistantActivity.class)));
        }
        if (cardFertilizerGuide != null) {
            cardFertilizerGuide.setOnClickListener(v -> startActivity(new Intent(requireContext(), FertilizerGuideActivity.class)));
        }
        if (tvFarmOverview != null) {
            tvFarmOverview.setOnClickListener(v -> getParentFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, new MyFarmFragment()).commit());
        }
    }

    private void setGreeting() {
        if (tvGreeting != null) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            String greeting;
            if (hour >= 5 && hour < 12) {
                greeting = "Good Morning, Mohit 👋";
            } else if (hour >= 12 && hour < 17) {
                greeting = "Good Afternoon, Mohit ☀️";
            } else if (hour >= 17 && hour < 21) {
                greeting = "Good Evening, Mohit 🌇";
            } else {
                greeting = "Good Night, Mohit 🌙";
            }
            tvGreeting.setText(greeting);
        }
    }

    private void getWeather(String cityName) {
        if (!isAdded() || getContext() == null) return;

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + API_KEY;

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            if (!isAdded()) return;
            try {
                String temp = response.getJSONObject("main").getString("temp");
                String humidity = response.getJSONObject("main").getString("humidity");
                String wind = response.getJSONObject("wind").getString("speed");

                JSONArray weatherArray = response.getJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);
                String condition = weather.getString("main");

                if (tvTemperature != null) tvTemperature.setText(temp + "°C");
                if (tvHumidity != null) tvHumidity.setText(humidity + "%");
                if (tvWindSpeed != null) tvWindSpeed.setText(wind + " km/h");
                if (tvWeather != null) tvWeather.setText(condition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            if (isAdded()) {
                Toast.makeText(requireContext(), "Weather Not Found", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}
