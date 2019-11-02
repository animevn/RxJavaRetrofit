package com.haanhgs.app.rxjavasimple.repo;

import com.haanhgs.app.rxjavasimple.model.flickr.Flickr;
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

    @GET("rest/")
    Observable<Flickr> getFlickr(
        @Query("method") String method,
        @Query("group_id") String group,
        @Query("api_key") String api,
        @Query("lat") Double lat,
        @Query("lon") Double lon,
        @Query("radius") String radius,
        @Query("format") String format,
        @Query("nojsoncallback") String nojsoncallback
    );
}
