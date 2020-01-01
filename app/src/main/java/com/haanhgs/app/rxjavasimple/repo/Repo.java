package com.haanhgs.app.rxjavasimple.repo;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repo {

    public static String convertEpocToDate(Double epoc){
        Date date = new Date((long)(epoc * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public static String convertEpocToDay(Double epoc){
        Date date = new Date((long)(epoc * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        return sdf.format(date);
    }

    public static String convertEpocToDayHour(Double epoc){
        Date date = new Date((long)(epoc * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public static RequestInterface initInterface(String url) {
        return new Retrofit.Builder().baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface.class);
    }

    public static int getId(Context context, String img){
        return context.getResources().getIdentifier("i" + img, "drawable", context.getPackageName());
    }

}
