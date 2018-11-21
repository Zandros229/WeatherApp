package com.example.ernest.weatherapp;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String BaseUrl = "https://openweathermap.org/";
    public static String AppId = "b6907d289e10d714a6e88b30761fae22";
    public static String lat = "-33.8";
    public static String lon = "151.2";

    private TextView country;
    private TextView city;
    private TextView temp;
    private TextView sky;
    private TextView humindity;
    private TextView pressure;
    private ImageView imageTemp;
    private ImageView imageViewHumnindity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        country = findViewById(R.id.Country);
        city = findViewById(R.id.City);
        temp=findViewById(R.id.Temperature);
        sky=findViewById(R.id.sky);
        humindity=findViewById(R.id.Humindity);
        pressure=findViewById(R.id.Preassure);
        imageTemp=findViewById(R.id.imageViewTemp);
        imageViewHumnindity=findViewById(R.id.imageViewHumindity);
        getCurrentData();
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

                    country.setText("Country "+weatherResponse.sys.country);
                    city.setText("City "+weatherResponse.name);
                    temp.setText("Temp " +String.valueOf(weatherResponse.main.temp));
                    sky.setText("sky "+weatherResponse.weather[0].description);
                    humindity.setText("humindity: "+ String.valueOf(weatherResponse.main.humidity));
                    pressure.setText("pressure "+String.valueOf(weatherResponse.main.pressure));
                    if(weatherResponse.main.temp>20)
                        imageTemp.setImageDrawable(getResources().getDrawable(R.drawable.hot));
                    if(weatherResponse.main.humidity<50)
                        imageViewHumnindity.setImageDrawable(getResources().getDrawable(R.drawable.lowrain));


                }
                else {
                    country.setText(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                country.setText(t.getMessage());
                country.setText("cos sie popsulo");
            }
        });
    }
}
