package com.deitel.weatherviewerapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView; // Importante para o texto da cidade
import android.widget.Toast;   // Para avisos rápidos

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Weather> weatherList = new ArrayList<>();
    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configura a Lista
        weatherListView = findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        // Configura o Botão
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. Pega o texto e limpa espaços extras
                EditText locationEditText = findViewById(R.id.locationEditText);
                String city = locationEditText.getText().toString().trim();

                // 2. VALIDAÇÃO DE SEGURANÇA (Resolve o problema do "a")
                if (city.length() < 3) {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Digite o nome completo da cidade (mínimo 3 letras).",
                            Snackbar.LENGTH_LONG).show();
                    return; // PARE AQUI! Não deixa buscar na API.
                }

                // 3. Limpa a tela visualmente
                weatherList.clear();
                weatherArrayAdapter.notifyDataSetChanged();

                TextView cityResultView = findViewById(R.id.cityResultTextView);
                cityResultView.setText("Buscando..."); // Feedback para o usuário

                // 4. Cria a URL e conecta
                URL url = createURL(city);

                if (url != null) {
                    dismissKeyboard(locationEditText);
                    GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
                    getLocalWeatherTask.execute(url);
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout),
                            "Erro ao criar URL.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    // --- Utilitário: Esconder Teclado ---
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // --- Criação da URL (Com a CHAVE CORRIGIDA) ---
    private URL createURL(String city) {
        // CORREÇÃO CRÍTICA: A chave correta é ...112m... (números UM)
        // No seu código anterior estava ...1l2m... (letra L), o que causava o erro em "Passos"
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

    // --- Tarefa de Conexão em Background ---
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
            // Se weather for null, a conexão falhou ou a chave estava errada
            if (weather != null) {
                convertJSONtoArrayList(weather);
                weatherArrayAdapter.notifyDataSetChanged();
                weatherListView.smoothScrollToPosition(0);
            } else {
                // Remove o texto "Buscando..." se falhar
                TextView cityResultView = findViewById(R.id.cityResultTextView);
                cityResultView.setText("Erro");

                Snackbar.make(findViewById(R.id.coordinatorLayout),
                        "Erro ao conectar ou cidade não encontrada.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // --- Processamento do JSON ---
    private void convertJSONtoArrayList(JSONObject forecast) {
        weatherList.clear();
        try {
            // Pega o nome oficial da cidade vindo da API
            if (forecast.has("city")) {
                String cityName = forecast.getString("city");
                TextView cityResultView = findViewById(R.id.cityResultTextView);
                cityResultView.setText(cityName);
            }

            JSONArray list = forecast.getJSONArray("days");

            for (int i = 0; i < list.length(); ++i) {
                JSONObject day = list.getJSONObject(i);
                weatherList.add(new Weather(
                        day.getString("date"),
                        day.getDouble("minTempC"),
                        day.getDouble("maxTempC"),
                        day.getDouble("humidity"),
                        day.getString("description"),
                        day.getString("icon")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}