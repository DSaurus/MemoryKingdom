package com.example.sao.helloworld;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by sao on 2017/3/2.
 */

public class FinishFragment extends Fragment {
    public Mylistener listener;
    public interface Mylistener
    {
        public void finishsend(String str);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentfinish, container, false);
        listener = (Mylistener) getActivity();
        Button finish = (Button) view.findViewById(R.id.returnmain);
        final String str = getArguments().get("data")+"";
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.finishsend(str);
            }
        });
        return view;
    }
}
