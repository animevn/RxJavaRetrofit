package com.haanhgs.app.rxjavasimple.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.haanhgs.app.rxjavasimple.R;
import com.haanhgs.app.rxjavasimple.model.flickr.Flickr;
import com.haanhgs.app.rxjavasimple.model.flickr.Photo;
import com.haanhgs.app.rxjavasimple.model.weather.CurrentWeather;
import com.haanhgs.app.rxjavasimple.model.weather.ListHour;
import com.haanhgs.app.rxjavasimple.model.weather.OpenWeather;
import com.haanhgs.app.rxjavasimple.repo.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static com.haanhgs.app.rxjavasimple.repo.Repo.initInterface;

public class Home extends Fragment {

    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.ivIconHome)
    ImageView ivIconHome;
    @BindView(R.id.tvTemp)
    TextView tvTemp;
    @BindView(R.id.tvMin)
    TextView tvMin;
    @BindView(R.id.tvMax)
    TextView tvMax;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.rvHour)
    RecyclerView rvHour;
    @BindView(R.id.rvDay)
    RecyclerView rvDay;
    @BindView(R.id.clHome)
    ConstraintLayout clHome;
    @BindView(R.id.ibRefresh)
    ImageButton ibRefresh;
    @BindView(R.id.ibLocation)
    ImageButton ibLocation;

    private static final String FLICKR_API = "8af3f7e81985dfc0c684d0cb200537e6";
    private static final String FLICKR_URL = "https://www.flickr.com/services/";
    private static final String METHOD = "flickr.photos.search";
    private static final String GROUP = "1463451@N25";
    private static final String RADIUS = "10";
    private static final String FORMAT = "json";
    private static final String NOJSONCALLBACK = "1";

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String API = "1a9dc82f0a3a7e535acb3ac84407ad81";

    private static final int REQUEST_CODE = 1;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int GRANTED = PackageManager.PERMISSION_GRANTED;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isTracking = true;
    private double lat;
    private double lon;

    private Context context;
    private Activity activity;
    private CompositeDisposable composite;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        activity = getActivity();
    }



    private void initRecyclerView(View view) {
        rvHour.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rvHour.setItemAnimator(new DefaultItemAnimator());
        rvDay.setLayoutManager(new LinearLayoutManager(context));
        rvDay.setItemAnimator(new DefaultItemAnimator());
    }

    private void handleHourlyForecast(OpenWeather weather) {
        AdapterHour adapter = new AdapterHour(context, weather.getList());
        rvHour.setAdapter(adapter);
        List<ListHour> listDay = new ArrayList<>();
        for (int i = 0; i < weather.getList().size(); i++) {
            if (weather.getList().get(i).getDtTxt().contains("09:00:00")) {
                listDay.add(weather.getList().get(i));
            }
        }
        AdapterDay adapterDay = new AdapterDay(context, listDay);
        rvDay.setAdapter(adapterDay);
    }

    private void handleCurrentWeather(CurrentWeather weather) {
        tvCity.setText(weather.getName());
        tvTemp.setText(String.format(Locale.getDefault(), "%.0f", weather.getMain().getTempC()));
        tvMin.setText(String.format(Locale.getDefault(), "%.0f", weather.getMain().getMinC()));
        tvMax.setText(String.format(Locale.getDefault(), "%.0f", weather.getMain().getMaxC()));
        tvDescription.setText(weather.getWeather().get(0).getDescription());
        String img = weather.getWeather().get(0).getIcon();
        int id = Repo.getId(context, img);
        Glide.with(context).load("").apply(RequestOptions.placeholderOf(id)).into(ivIconHome);
    }

    private String getImageUrl(List<Photo> list) {
        String url = "";
        if (list.size() > 0) {
            int random = (new Random()).nextInt(list.size());
            String farm = String.valueOf(list.get(random).getFarm());
            String server = list.get(random).getServer();
            String id = list.get(random).getId();
            String secret = list.get(random).getSecret();
            url = "https://farm" + farm + ".staticflickr.com/" + server
                    + "/" + id + "_" + secret + ".jpg";
        }
        return url;
    }

    private void handleFlickr(Flickr flickr) {
        List<Photo> list = new ArrayList<>();
        List<Photo> temp = flickr.getPhotos().getPhoto();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getIspublic() == 1) {
                list.add(temp.get(i));
            }
        }
        Glide.with(context).load(Uri.parse(getImageUrl(list))).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource,
                                        @Nullable Transition<? super Drawable> transition) {
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

    private void loadHourlyForecast() {
        Disposable disposable = initInterface(WEATHER_URL).getHourlyWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleHourlyForecast, this::handleError);
        composite.add(disposable);
    }

    private void loadCurrentWeather() {
        Disposable disposable = initInterface(WEATHER_URL).getCurrentWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCurrentWeather, this::handleError);
        composite.add(disposable);
    }

    private void loadFlickr() {
        Disposable disposable = initInterface(FLICKR_URL)
                .getFlickr(METHOD, GROUP, FLICKR_API, lat, lon, RADIUS, FORMAT, NOJSONCALLBACK)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFlickr, this::handleError);
        composite.add(disposable);
    }

    private void loadWeather(Location location){
        lat = location.getLatitude();
        lon = location.getLongitude();
        loadHourlyForecast();
        loadCurrentWeather();
        loadFlickr();
    }

    private void initFuseLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {  Log.d("D.Home", " gps not null");
                loadWeather(location);
                Log.d("D.Home", "" + lat + " - " + lon);
            } else {
                startTrackingLocation();
            }
        });
    }

    private LocationRequest locationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(context, FINE_LOCATION) != GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{FINE_LOCATION}, REQUEST_CODE);
        } else {
            isTracking = true;
            fusedLocationClient.requestLocationUpdates(locationRequest(), locationCallback, null);
        }
    }

    private void stopTrackingLocation() {
        if (isTracking) {
            isTracking = false;
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void initLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (isTracking){
                    Location location = locationResult.getLastLocation();
                    loadWeather(location);
                    stopTrackingLocation();
                }
            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        composite = new CompositeDisposable();
        initRecyclerView(view);
        initFuseLocation();
        initLocationCallback();
        return view;
    }

    @Override
    public void onPause() {
        if (isTracking) {
            stopTrackingLocation();
            isTracking = true;
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (isTracking) {
            startTrackingLocation();
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        composite.clear();
    }

    @OnClick({R.id.ibRefresh, R.id.ibLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibRefresh:
                initFuseLocation();
                break;
            case R.id.ibLocation:
                startTrackingLocation();
                break;
        }
    }
}
