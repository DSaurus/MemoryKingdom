package com.example.sao.helloworld;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CountryFragment.Mylistener,
        BeisongFragment.Mylistener, FinishFragment.Mylistener, LearnFragment.Mylistener, LoadFragment.Mylistener{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.replace(R.id.main, new LearnFragment());
        begin.commit();
    }

    @Override
    public void CFtoBF(int x, String str) {
        BeisongFragment BF = new BeisongFragment();
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putString("number", x+"");
        bundle.putString("data", str);
        BF.setArguments(bundle);

        begin.replace(R.id.main, BF);
        begin.commit();
    }
    public void CFtoMF(String str) {
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.addToBackStack(null);

        MemoryFragment MF = new MemoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        MF.setArguments(bundle);

        begin.replace(R.id.main, MF);
        begin.commit();
    }
    //完成任务显示完成页面
    public void BFtoFF(String str)
    {
        getFragmentManager().popBackStack();
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.addToBackStack(null);
        FinishFragment FF = new FinishFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        FF.setArguments(bundle);

        begin.replace(R.id.main, FF);
        begin.commit();
    }
    //回到课程页面
    public void FFtoCF(String str)
    {
        getFragmentManager().popBackStack();
    }
    public void LFtoloadF()
    {
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.addToBackStack(null);
        begin.replace(R.id.main, new LoadFragment());
        begin.commit();
    }

    public void loadFtoLF() {
        getFragmentManager().popBackStack();
    }

    public void LFtoCF(String str)
    {
        CountryFragment CF = new CountryFragment();
        FragmentTransaction begin = getFragmentManager().beginTransaction();
        begin.addToBackStack(null);

        Bundle bundle = new Bundle();
        bundle.putString("data", str);
        CF.setArguments(bundle);

        begin.replace(R.id.main, CF);
        begin.commit();
    }
}
