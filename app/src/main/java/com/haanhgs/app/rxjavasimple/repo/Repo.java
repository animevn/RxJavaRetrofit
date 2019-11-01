package com.haanhgs.app.rxjavasimple.repo;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Repo {

    public static String convertEpocToDate(Double epoc){
        Date date = new Date((long)(epoc * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh:mm", Locale.getDefault());
        return sdf.format(date);
    }

    public static int getId(Context context, String img){
        return context.getResources().getIdentifier("i" + img, "drawable", context.getPackageName());
    }

}
