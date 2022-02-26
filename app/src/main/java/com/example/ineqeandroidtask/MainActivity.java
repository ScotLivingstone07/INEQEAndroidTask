package com.example.ineqeandroidtask;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView weatherReport;
    DecimalFormat df = new DecimalFormat("#.##");
    //Url for the current days forecast
    private final String urlCurrent = "https://api.openweathermap.org/data/2.5/weather";
    //Url for the next five days forecast
    private final String url5Days = "https://api.openweathermap.org/data/2.5/forecast";
    //API code
    private final String apiid = "c081cae5be86dcf01a019231dab2dcc2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.city);
        weatherReport = findViewById(R.id.weatherreport);
    }
    //Code for the Current days weather
    public void getWeatherCurrentDay(View view) {
        String tempUrlCurrent = "";
        String cityCurrent = city.getText().toString().trim();
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timeconvert = new SimpleDateFormat("hh:mm  aa");
        //IF statement to check if the user has entered a city
        if (cityCurrent.equals("")) {
            weatherReport.setText("You have not entered a city");
        } else {
            //Creation of the correct url
            tempUrlCurrent = urlCurrent + "?q=" + cityCurrent + "&appid=" + apiid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrlCurrent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double minTemp = jsonObjectMain.getDouble("temp_min") - 273.15;
                    double maxTemp = jsonObjectMain.getDouble("temp_max") - 273.15;
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    int sunrise = jsonObjectSys.getInt("sunrise");
                    int sunset = jsonObjectSys.getInt("sunset");
                    String cityName = jsonResponse.getString("name");
                    int time = jsonResponse.getInt("dt");
                    //Time Convert for sunrise
                    long epochSunrise = Long.parseLong(String.valueOf(sunrise));
                    Date sunrise12hr= new Date( epochSunrise * 1000 );
                    //Time Convert for sunset
                    long epochSunset = Long.parseLong(String.valueOf(sunset));
                    Date sunset12hr= new Date( epochSunset * 1000 );
                    //Time Convert to find out the day
                    long epochTime = Long.parseLong(String.valueOf(time));
                    Date day= new Date( epochTime * 1000 );
                    Calendar c = Calendar.getInstance();
                    c.setTime(day);
                    String dayText = new SimpleDateFormat("EEEE").format(day);

                    output +=  dayText + " " + date.format(day)
                            + "\n Min Temperature: " + df.format(minTemp) + " °C"
                            + "\n Max Temperature: " + df.format(maxTemp) + " °C"
                            + "\n Sunrise: " + timeconvert.format(sunrise12hr)
                            + "\n Sunset: " + timeconvert.format(sunset12hr)
                            ;
                    weatherReport.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // Response if city is invalid
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                //Error message for user when city cant be found
                weatherReport.setText("Cant find weather information for the city please try again");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    //Code for the weather over other next five days
    public void getWeather5days(View view) {
        String tempUrl5Days = "";
        String city5Days = city.getText().toString().trim();
        //IF statement to check if the user has entered a city
        if(city5Days.equals("")){
            weatherReport.setText("You have not entered a city");
        }else{
            //Creation of the correct url
            tempUrl5Days = url5Days + "?q=" + city5Days + "&appid=" + apiid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl5Days, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("list");
                    //Information for day 1
                    JSONObject dailyForecast = jsonArray.getJSONObject(0);
                    int time = dailyForecast.getInt("dt");
                    JSONObject jsonObjectMain = dailyForecast.getJSONObject("main");
                    double minTemp = jsonObjectMain.getDouble("temp_min") - 273.15;
                    double maxTemp = jsonObjectMain.getDouble("temp_max") - 273.15;
                    JSONObject jsonObjectWind = dailyForecast.getJSONObject("wind");
                    double speed = jsonObjectWind.getInt("speed");
                    double direction = jsonObjectWind.getInt("deg");
                    //Information for day 2
                    JSONObject dailyForecast2 = jsonArray.getJSONObject(7);
                    int time2 = dailyForecast2.getInt("dt");
                    JSONObject jsonObjectMain2 = dailyForecast2.getJSONObject("main");
                    double minTemp2 = jsonObjectMain2.getDouble("temp_min") - 273.15;
                    double maxTemp2 = jsonObjectMain2.getDouble("temp_max") - 273.15;
                    JSONObject jsonObjectWind2 = dailyForecast2.getJSONObject("wind");
                    double speed2 = jsonObjectWind2.getInt("speed");
                    double direction2 = jsonObjectWind2.getInt("deg");
                    //Information for day 3
                    JSONObject dailyForecast3 = jsonArray.getJSONObject(15);
                    int time3 = dailyForecast3.getInt("dt");
                    JSONObject jsonObjectMain3 = dailyForecast3.getJSONObject("main");
                    double minTemp3 = jsonObjectMain3.getDouble("temp_min") - 273.15;
                    double maxTemp3 = jsonObjectMain3.getDouble("temp_max") - 273.15;
                    JSONObject jsonObjectWind3 = dailyForecast3.getJSONObject("wind");
                    double speed3 = jsonObjectWind3.getInt("speed");
                    double direction3 = jsonObjectWind3.getInt("deg");
                    //Information for day 4
                    JSONObject dailyForecast4 = jsonArray.getJSONObject(23);
                    int time4 = dailyForecast4.getInt("dt");
                    JSONObject jsonObjectMain4 = dailyForecast4.getJSONObject("main");
                    double minTemp4 = jsonObjectMain4.getDouble("temp_min") - 273.15;
                    double maxTemp4 = jsonObjectMain4.getDouble("temp_max") - 273.15;
                    JSONObject jsonObjectWind4 = dailyForecast4.getJSONObject("wind");
                    double speed4 = jsonObjectWind4.getInt("speed");
                    double direction4 = jsonObjectWind4.getInt("deg");
                    //Information for day 5
                    JSONObject dailyForecast5 = jsonArray.getJSONObject(31);
                    int time5 = dailyForecast5.getInt("dt");
                    JSONObject jsonObjectMain5 = dailyForecast5.getJSONObject("main");
                    double minTemp5 = jsonObjectMain5.getDouble("temp_min") - 273.15;
                    double maxTemp5 = jsonObjectMain5.getDouble("temp_max") - 273.15;
                    JSONObject jsonObjectWind5 = dailyForecast5.getJSONObject("wind");
                    double speed5 = jsonObjectWind5.getInt("speed");
                    double direction5 = jsonObjectWind5.getInt("deg");
                    //Time Convert to find out the day
                    long epochTime = Long.parseLong(String.valueOf(time));
                    Date day= new Date( epochTime * 1000 );
                    Calendar c = Calendar.getInstance();
                    c.setTime(day);
                    String dayText = new SimpleDateFormat("EEEE").format(day);
                    //Time Convert to find out the day
                    long epochTime2 = Long.parseLong(String.valueOf(time2));
                    Date day2= new Date( epochTime2 * 1000 );
                    Calendar c2 = Calendar.getInstance();
                    c2.setTime(day2);
                    String dayText2 = new SimpleDateFormat("EEEE").format(day2);
                    //Time Convert to find out the day
                    long epochTime3 = Long.parseLong(String.valueOf(time3));
                    Date day3= new Date( epochTime3 * 1000 );
                    Calendar c3 = Calendar.getInstance();
                    c3.setTime(day3);
                    String dayText3 = new SimpleDateFormat("EEEE").format(day3);
                    //Time Convert to find out the day
                    long epochTime4 = Long.parseLong(String.valueOf(time4));
                    Date day4= new Date( epochTime4 * 1000 );
                    Calendar c4 = Calendar.getInstance();
                    c4.setTime(day4);
                    String dayText4 = new SimpleDateFormat("EEEE").format(day4);
                    //Time Convert to find out the day
                    long epochTime5 = Long.parseLong(String.valueOf(time5));
                    Date day5= new Date( epochTime5 * 1000 );
                    Calendar c5 = Calendar.getInstance();
                    c5.setTime(day5);
                    String dayText5 = new SimpleDateFormat("EEEE").format(day5);


                    output += dayText
                            +"\nMin Temperature: " + df.format(minTemp) + " °C"
                            + "\nMax Temperature: " + df.format(maxTemp) + " °C"
                            + "\nWind Speed: " + speed + " mph"
                            + "\nWind Direction: " + direction + " °"
                            + "\n"
                            + "\n" + dayText2
                            + "\nMin Temperature: " + df.format(minTemp2) + " °C"
                            + "\nMax Temperature: " + df.format(maxTemp2) + " °C"
                            + "\nWind Speed: " + speed2 + " mph"
                            + "\nWind Direction: " + direction2 + " °"
                            + "\n"
                            + "\n" + dayText3
                            + "\nMin Temperature: " + df.format(minTemp3) + " °C"
                            + "\nMax Temperature: " + df.format(maxTemp3) + " °C"
                            + "\nWind Speed: " + speed3 + " mph"
                            + "\nWind Direction: " + direction3 + " °"
                            + "\n"
                            + "\n" + dayText4
                            + "\nMin Temperature: " + df.format(minTemp4) + " °C"
                            + "\nMax Temperature: " + df.format(maxTemp4) + " °C"
                            + "\nWind Speed: " + speed4 + " mph"
                            + "\nWind Direction: " + direction4 + " °"
                            + "\n"
                            + "\n" + dayText5
                            + "\nMin Temperature: " + df.format(minTemp5) + " °C"
                            + "\nMax Temperature: " + df.format(maxTemp5) + " °C"
                            + "\nWind Speed: " + speed5 + " mph"
                            + "\nWind Direction: " + direction5 + " °"
                    ;
                    weatherReport.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // Response if city is invalid
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                //Error message for user when city cant be found
                weatherReport.setText("Cant find weather information for the city please try again");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
