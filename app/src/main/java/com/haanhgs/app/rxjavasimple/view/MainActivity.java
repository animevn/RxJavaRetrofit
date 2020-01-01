package com.haanhgs.app.rxjavasimple.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.haanhgs.app.rxjavasimple.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)getSupportActionBar().hide();
    }
}
