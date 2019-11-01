package com.haanhgs.app.rxjavasimple.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.haanhgs.app.rxjavasimple.R;
import com.haanhgs.app.rxjavasimple.model.CurrentWeather;
import com.haanhgs.app.rxjavasimple.model.ListHour;
import com.haanhgs.app.rxjavasimple.model.OpenWeather;
import com.haanhgs.app.rxjavasimple.repo.Repo;
import com.haanhgs.app.rxjavasimple.repo.RequestInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends Fragment {

    private static final String url = "https://api.openweathermap.org/data/2.5/";
    private static final String API = "1a9dc82f0a3a7e535acb3ac84407ad81";
    private static final double lat = 21.028511;
    private static final double lon = 105.804817;

    private TextView tvDescription;
    private TextView tvTemp;
    private TextView tvMin;
    private TextView tvMax;
    private TextView tvCity;
    private ImageView ivIconHome;

    private Context context;
    private List<ListHour> list;
    private RecyclerView rvMain;
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
    }

    private void initRecyclerView(View view){
        rvMain = view.findViewById(R.id.rvHour);
        rvMain.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        rvMain.setItemAnimator(new DefaultItemAnimator());
    }

    private RequestInterface initInterface(){
        return new Retrofit.Builder().baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface.class);
    }

    private void handleHourlyForecast(OpenWeather weather) {
        Adapter adapter = new Adapter(context, weather.getList());
        rvMain.setAdapter(adapter);

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

    private void handleError(Throwable error) {
        Toast.makeText(context, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void loadHourlyForecast(){
        Disposable disposable = initInterface().getHourlyWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleHourlyForecast, this::handleError);
    }

    private void loadCurrentWeather(){
        Disposable disposable = initInterface().getCurrentWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCurrentWeather, this::handleError);
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
        return view;
    }
}
