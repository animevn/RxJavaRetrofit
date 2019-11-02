package com.haanhgs.app.rxjavasimple.repo;

import com.haanhgs.app.rxjavasimple.model.weather.CurrentWeather;
import com.haanhgs.app.rxjavasimple.model.weather.OpenWeather;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestInterface {

    @GET("forecast/")
    Observable<OpenWeather> getHourlyWeather(
            @Query("appid") String appid,
            @Query("lat") Double lat,
            @Query("lon") Double lon
    );

    @GET("weather/")
    Observable<CurrentWeather> getCurrentWeather(
            @Query("appid") String appid,
            @Query("lat") Double lat,
            @Query("lon") Double lon
    );
}
