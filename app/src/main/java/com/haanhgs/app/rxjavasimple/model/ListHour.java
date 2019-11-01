package com.haanhgs.app.rxjavasimple.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ListHour {

    @SerializedName("dt")
    @Expose
    private Double dt;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;

    public Double getDt() {
        return dt;
    }

    public void setDt(Double dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

//    @SerializedName("clouds")
//    @Expose
//    private Clouds clouds;

//    @SerializedName("wind")
//    @Expose
//    private Wind wind;

//    @SerializedName("sys")
//    @Expose
//    private Sys sys;



//    @SerializedName("rain")
//    @Expose
//    private Rain rain;



//    public Clouds getClouds() {
//        return clouds;
//    }
//
//    public void setClouds(Clouds clouds) {
//        this.clouds = clouds;
//    }
//
//    public Wind getWind() {
//        return wind;
//    }
//
//    public void setWind(Wind wind) {
//        this.wind = wind;
//    }


//    public Sys getSys() {
//        return sys;
//    }
//
//    public void setSys(Sys sys) {
//        this.sys = sys;
//    }



//    public Rain getRain() {
//        return rain;
//    }
//
//    public void setRain(Rain rain) {
//        this.rain = rain;
//    }

}
