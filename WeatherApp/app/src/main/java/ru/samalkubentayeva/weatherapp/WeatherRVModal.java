package ru.samalkubentayeva.weatherapp;

public class WeatherRVModal {

    private String time;
    private String temperature;
    private String icon;
    private String uvIndex;

    public WeatherRVModal(String time, String temperature, String icon, String uvIndex) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.uvIndex = uvIndex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
    }

}
