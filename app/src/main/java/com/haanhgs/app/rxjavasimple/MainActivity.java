package com.haanhgs.app.rxjavasimple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.os.Bundle;
import android.widget.Toast;
import com.haanhgs.app.rxjavasimple.model.ListHour;
import com.haanhgs.app.rxjavasimple.model.OpenWeather;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String url = "https://api.openweathermap.org/data/2.5/";
    private static final String API = "1a9dc82f0a3a7e535acb3ac84407ad81";
    private static final double lat = 21.028511;
    private static final double lon = 105.804817;

    private OpenWeather weather;
    private List<ListHour>list;
    private Adapter adapter;
    private RecyclerView rvMain;
    private CompositeDisposable composite;

    private void initRecyclerView(){
        rvMain = findViewById(R.id.rvMain);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setItemAnimator(new DefaultItemAnimator());
        rvMain.setHasFixedSize(true);
    }

    private void handleResponse(OpenWeather weather) {
        this.weather = weather;
        adapter = new Adapter(this, weather.getList());
        rvMain.setAdapter(adapter);
    }

    private void handleError(Throwable error) {
        Toast.makeText(this, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void loadData(){
        RequestInterface requestInterface = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface.class);
        Disposable disposable = requestInterface.getHourlyWeather(API, lat, lon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        loadData();


    }
}
