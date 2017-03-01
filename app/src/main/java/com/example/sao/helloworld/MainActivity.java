package com.example.sao.helloworld;

import android.app.FragmentTransaction;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CountryFragment.Mylistener {

    CountryFragment CF = new CountryFragment();
    BeisongFragment BF = new BeisongFragment();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.add(R.id.main, CF);
        begin.commit();
    }

    @Override
    public void thank(int x) {
        if(x == 1)
        {
            FragmentTransaction begin = getFragmentManager().beginTransaction();
            begin.replace(R.id.main, BF);
            begin.commit();
            //Toast.makeText(this, "wtf", Toast.LENGTH_LONG).show();
        }
    }
}
