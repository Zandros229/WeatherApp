package com.example.ernest.weatherapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String BaseUrl = "https://openweathermap.org/";
    public static String AppId = "b6907d289e10d714a6e88b30761fae22";
    public static String lat = "51.75";
    public static String lon = "19.47";

    private TextView weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherData = findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                getCurrentData();
            }
        });
    }

    void getCurrentData()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseUrl)
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call call = service.getCurrentWeatherData(lat, lon, AppId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.code() == 200)
                {
                    WeatherResponse weatherResponse = (WeatherResponse) response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "City:" +
                            weatherResponse.name +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.temp_max +
                            "\n" +
                            "Sky: " +
                            weatherResponse.weather[0].description +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure;
                    weatherData.setText(stringBuilder);
                }
                else {
                    weatherData.setText(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                weatherData.setText(t.getMessage());
                weatherData.setText("cos sie popsulo");
            }
        });
    }
}
