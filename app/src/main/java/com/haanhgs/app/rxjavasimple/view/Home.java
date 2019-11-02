package com.haanhgs.app.rxjavasimple.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haanhgs.app.rxjavasimple.R;
import com.haanhgs.app.rxjavasimple.model.flickr.Flickr;
import com.haanhgs.app.rxjavasimple.model.flickr.Photo;
import com.haanhgs.app.rxjavasimple.model.weather.CurrentWeather;
import com.haanhgs.app.rxjavasimple.model.weather.ListHour;
import com.haanhgs.app.rxjavasimple.model.weather.OpenWeather;
import com.haanhgs.app.rxjavasimple.repo.Repo;
import com.haanhgs.app.rxjavasimple.repo.RequestInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends Fragment {

    private static final String FLICKR_API = "8af3f7e81985dfc0c684d0cb200537e6";
    private static final String FLICKR_URL = "https://www.flickr.com/services/";
    private static final String METHOD = "flickr.photos.search";
    private static final String GROUP = "1463451@N25";
    private static final String RADIUS = "10";
    private static final String FORMAT = "json";
    private static final String NOJSONCALLBACK = "1";

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API = "1a9dc82f0a3a7e535acb3ac84407ad81";
    private static final double lat = 21.028511;
    private static final double lon = 105.804817;

    private TextView tvDescription;
    private TextView tvTemp;
    private TextView tvMin;
    private TextView tvMax;
    private TextView tvCity;
    private ImageView ivIconHome;
    private ConstraintLayout clHome;

    private Context context;
    private List<ListHour> list;
    private RecyclerView rvMain;
    private RecyclerView rvDay;
    private CompositeDisposable composite;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initViews(View view){
        tvDescription = view.findViewById(R.id.tvDescription);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvMin = view.findViewById(R.id.tvMin);
        tvMax = view.findViewById(R.id.tvMax);
        tvCity = view.findViewById(R.id.tvCity);
        ivIconHome = view.findViewById(R.id.ivIconHome);
        clHome = view.findViewById(R.id.clHome);
    }

    private void initRecyclerView(View view){
        rvMain = view.findViewById(R.id.rvHour);
        rvMain.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rvMain.setItemAnimator(new DefaultItemAnimator());

        rvDay = view.findViewById(R.id.rvDay);
        rvDay.setLayoutManager(new LinearLayoutManager(context));
        rvDay.setItemAnimator(new DefaultItemAnimator());


    }

    private RequestInterface initInterface(String url){
        return new Retrofit.Builder().baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface.class);
    }

    private void handleHourlyForecast(OpenWeather weather) {
        AdapterHour adapter = new AdapterHour(context, weather.getList());
        rvMain.setAdapter(adapter);
        List<ListHour> listDay = new ArrayList<>();
        for (int i = 0; i < weather.getList().size(); i++){
            if (weather.getList().get(i).getDtTxt().contains("09:00:00")){
                listDay.add(weather.getList().get(i));
            }
        }
        AdapterDay adapterDay = new AdapterDay(context, listDay);
        rvDay.setAdapter(adapterDay);
    }

    private void handleCurrentWeather(CurrentWeather weather){
        tvCity.setText(weather.getName());
        tvTemp.setText(String.format(Locale.getDefault(), "%.0f", weather.getMain().getTempC()));
        tvMin.setText(String.format(Locale.getDefault(), "%.0f", weather.getMain().getMinC()));
        tvMax.setText(String.format(Locale.getDefault(), "%.0f", weather.getMain().getMaxC()));
        tvDescription.setText(weather.getWeather().get(0).getDescription());
        String img = weather.getWeather().get(0).getIcon();
        int id = Repo.getId(context, img);
        Glide.with(context).load("").apply(RequestOptions.placeholderOf(id)).into(ivIconHome);
    }

    private void handleFlickr(Flickr flickr){
        List<Photo>list = new ArrayList<>();
        List<Photo>temp = flickr.getPhotos().getPhoto();
        for (int i = 0; i < temp.size(); i++){
            if (temp.get(i).getIspublic() == 1){
                list.add(temp.get(i));
            }
        }
        String url = "";
        if (list.size() > 0){
            int random = (new Random()).nextInt(list.size());
            String farm = String.valueOf(list.get(random).getFarm());
            String server = list.get(random).getServer();
            String id = list.get(random).getId();
            String secret = list.get(random).getSecret();
            url = "https://farm" + farm + ".staticflickr.com/" + server
                    + "/" + id + "_" + secret + ".jpg";
        }
        Glide.with(context)
                .load(Uri.parse(url))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition){
                        clHome.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void handleError(Throwable error) {
        Toast.makeText(context, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void loadHourlyForecast(){
        Disposable disposable = initInterface(WEATHER_URL).getHourlyWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleHourlyForecast, this::handleError);
    }

    private void loadCurrentWeather(){
        Disposable disposable = initInterface(WEATHER_URL).getCurrentWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCurrentWeather, this::handleError);
    }

    private void loadFlickr(){
        Disposable disposable = initInterface(FLICKR_URL)
                .getFlickr(METHOD, GROUP, FLICKR_API, lat, lon, RADIUS, FORMAT, NOJSONCALLBACK)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFlickr, this::handleError);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initRecyclerView(view);
        initViews(view);
        loadHourlyForecast();
        loadCurrentWeather();
        loadFlickr();
        return view;
    }
}
