package ru.samalkubentayeva.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {

        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.setDetails(modal);
//        String text = modal.getTemperature() + "°c";
//        holder.temperatureTV.setText(text);
//        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionIV);
//        holder.uvIndexTV.setText(modal.getUvIndex());
//        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
//        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa", Locale.US);
//        try{
//            Date t = input.parse(modal.getTime());
//            assert t != null;
//            holder.timeTV.setText(output.format(t));
//        } catch (ParseException e){
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView uvIndexTV;
        private TextView temperatureTV;
        private TextView timeTV;
        private ImageView conditionIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uvIndexTV = itemView.findViewById(R.id.idTVUvIndex);
            temperatureTV = itemView.findViewById(R.id.idTVTemperature);
            timeTV = itemView.findViewById(R.id.idTVTime);
            conditionIV = itemView.findViewById(R.id.idIVCondition);
        }

        public void setDetails(WeatherRVModal modal) {
            uvIndexTV.setText(modal.getUvIndex());
            temperatureTV.setText(modal.getTemperature() +"°c");
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US);
            SimpleDateFormat output = new SimpleDateFormat("hh:mm aa", Locale.US);
            try {
                Date t = input.parse(modal.getTime());
                assert t != null;
                timeTV.setText(output.format(t));
            } catch (ParseException e){
                e.printStackTrace();
            }
            Picasso.get().load("https:".concat(modal.getIcon())).into(conditionIV);
        }
    }
}
