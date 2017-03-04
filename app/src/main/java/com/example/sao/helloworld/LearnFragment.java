package com.example.sao.helloworld;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sao on 2017/3/3.
 */

public class LearnFragment extends Fragment{
    View view;
    public String read(InputStream fis) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1)
            outstream.write(buffer, 0, len);
        fis.close(); outstream.close();
        byte[] data = outstream.toByteArray();
        return new String(data);
    }
    String []content = new String[100];
    int nctt;
    ArrayAdapter<String> listadapter = null;
    String ESD = "storage/emulated/0/wtf/";
    public void transtocontent(String str)
    {
        nctt = 0;
        StringBuffer temp = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(ch == '!')
            {
                content[nctt++] = temp.toString();
                temp = new StringBuffer();
                continue;
            }
            if(ch != ' ') temp.append(ch);
        }
    }
    public void learninit()
    {
        File file = new File(ESD);
        if(!file.exists()) file.mkdirs();
        file = new File(file, "_learn.txt");
        if(file.exists())
        {
            String input = " ";
            try {
                input = read(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            transtocontent(input);
        } else
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface Mylistener
    {
        public void loadsend();
        public void learnsend(String str);
    }
    public Mylistener listener;

    ListView listview;
    String[] getcontentstr()
    {
        String[] temp = new String[nctt];
        for(int i = 0; i < nctt; i++) temp[i] = content[i];
        return temp;
    }
    public class Loadlistener implements View.OnClickListener {
        public void onClick(View view) {
            listener.loadsend();
        }
    }

    public class Listviewlistener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            listener.learnsend(listadapter.getItem(i));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentlearn, container, false);
        listener = (Mylistener)getActivity();
        learninit();
        //listview初始化
        listview = (ListView) view.findViewById(R.id.learnlist);
        TextView emptyView = (TextView) view.findViewById(R.id.learnempty);
        listview.setEmptyView(emptyView);

        if(nctt > 0) listadapter=new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getcontentstr());
        listview.setAdapter(listadapter);
        Button download = (Button) view.findViewById(R.id.learndownload);
        download.setOnClickListener(new Loadlistener());
        listview.setOnItemClickListener(new Listviewlistener());
        return view;
    }
}
