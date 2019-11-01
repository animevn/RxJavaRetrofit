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
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String url = "https://api.learn2crack.com/";
    private List<Android>list;
    private Adapter adapter;
    private RecyclerView rvMain;
    private CompositeDisposable composite;

    private void initRecyclerView(){
        rvMain = findViewById(R.id.rvMain);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setItemAnimator(new DefaultItemAnimator());
        rvMain.setHasFixedSize(true);
    }

    private void handleResponse(List<Android> androidList) {
        list = new ArrayList<>(androidList);
        adapter = new Adapter(list);
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
        Disposable disposable = requestInterface.register()
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
