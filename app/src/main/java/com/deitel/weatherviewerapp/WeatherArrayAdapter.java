package com.deitel.weatherviewerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class WeatherArrayAdapter extends ArrayAdapter<Weather> {


    private static class ViewHolder {
        TextView conditionTextView;
        TextView dayTextView;
        TextView lowTextView;
        TextView hiTextView;
        TextView humidityTextView;
    }


    public WeatherArrayAdapter(Context context, List<Weather> forecast) {
        super(context, -1, forecast);
    }

    // Método principal que monta cada linha da lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather day = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);

            viewHolder.conditionTextView = convertView.findViewById(R.id.conditionTextView);
            viewHolder.dayTextView = convertView.findViewById(R.id.dayTextView);
            viewHolder.lowTextView = convertView.findViewById(R.id.lowTextView);
            viewHolder.hiTextView = convertView.findViewById(R.id.hiTextView);
            viewHolder.humidityTextView = convertView.findViewById(R.id.humidityTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (day != null) {

            viewHolder.conditionTextView.setText(day.iconEmoji);

            viewHolder.dayTextView.setText(day.date + " - " + day.description);
            viewHolder.lowTextView.setText("Min: " + day.minTemp);
            viewHolder.hiTextView.setText("Max: " + day.maxTemp);
            viewHolder.humidityTextView.setText("Umidade: " + day.humidity);
        }

        return convertView;
    }
}