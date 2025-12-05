package com.deitel.weatherviewerapp;

import java.text.NumberFormat;

public class Weather {
    public final String date;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String iconEmoji;


    public Weather(String date, double minTemp, double maxTemp, double humidity, String description, String iconEmoji) {
        this.date = date;

        // Formata  °C
        this.minTemp = String.format("%.0f°C", minTemp);
        this.maxTemp = String.format("%.0f°C", maxTemp);


        this.humidity = NumberFormat.getPercentInstance().format(humidity);

        this.description = description;
        this.iconEmoji = iconEmoji;
    }
}