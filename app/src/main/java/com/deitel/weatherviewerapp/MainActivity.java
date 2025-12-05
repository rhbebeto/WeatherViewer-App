package com.deitel.weatherviewerapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<Weather> weatherList = new ArrayList<>();


    private WeatherArrayAdapter weatherArrayAdapter;

    // Referência para a lista na tela
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Coolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Lista
        weatherListView = findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        // Botão Flutuante
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText locationEditText = findViewById(R.id.locationEditText);
                String city = locationEditText.getText().toString();


                URL url = createURL(city);


                if (url != null) {
                    dismissKeyboard(locationEditText);
                    GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
                    getLocalWeatherTask.execute(url);
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "URL Inválida", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    // --- MÉTODO 1: Esconder o teclado (Utilitário) ---
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // --- MÉTODO 2: Criar a URL  ---
    private URL createURL(String city) {

        String apiKey = "AgentWeather2024_a8f3b9c1d7e2f5g6h4i9j0k1l2m3n4o5p6";
        String baseUrl = "http://agent-weathermap-env-env.eba-6pzgqekp.us-east-2.elasticbeanstalk.com/api/weather";

        try {

            String urlString = baseUrl + "?city=" + URLEncoder.encode(city, "UTF-8") +
                    "&days=7&APPID=" + apiKey;
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }

                    return new JSONObject(builder.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onPostExecute(JSONObject weather) {
            if (weather != null) {
                convertJSONtoArrayList(weather);
                weatherArrayAdapter.notifyDataSetChanged();
                weatherListView.smoothScrollToPosition(0);
            } else {
                Snackbar.make(findViewById(R.id.coordinatorLayout),
                        "Erro ao conectar ou cidade não encontrada.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // --- MÉTODO 3: Converter JSON (ADAPTAÇÃO DO TRABALHO) ---
    // O JSON  diferente do livro.
    private void convertJSONtoArrayList(JSONObject forecast) {
        weatherList.clear(); // Limpa a lista antiga
        try {
            // Pega o array "days" do JSON [cite: 1001]
            JSONArray list = forecast.getJSONArray("days");

            // Percorre cada dia da previsão
            for (int i = 0; i < list.length(); ++i) {
                JSONObject day = list.getJSONObject(i); // Pega o objeto do dia

                // Cria o objeto Weather com os campos específicos da atividade [cite: 1004-1009]
                weatherList.add(new Weather(
                        day.getString("date"),       // Data
                        day.getDouble("minTempC"),   // Mínima em Celsius
                        day.getDouble("maxTempC"),   // Máxima em Celsius
                        day.getDouble("humidity"),   // Umidade
                        day.getString("description"),// Descrição
                        day.getString("icon")        // O Emoji
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}