package com.example.sao.helloworld;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sao on 2017/2/25.
 */

public class CountryFragment extends Fragment {
    public class Data{
        public String ques, ans;
        public int id;
        public int state, val, time;
    }
    public String read() throws IOException {
        InputStream fis = getResources().openRawResource(R.raw.data);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fis.read(buffer)) != -1)
            outstream.write(buffer, 0, len);
        fis.close(); outstream.close();
        byte[] data = outstream.toByteArray();
        return new String(data);
    }

    public Mylistener listener;

    public interface Mylistener
    {
        public void thank(int x);
    }
    public void write(String output) throws IOException {
        File path = new File("storage/emulated/0/wtf");
        File file = new File(path, "data.txt");
        if(!file.exists())Toast.makeText(getActivity(), Environment.getExternalStorageDirectory().getPath(), Toast.LENGTH_LONG).show();
        FileOutputStream fis = new FileOutputStream(file);
        byte[] words = output.getBytes();
        fis.write(words);
        fis.close();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentcountry, container, false);
        Button tasks = (Button) view.findViewById(R.id.todaytask);
        Button download = (Button) view.findViewById(R.id.download);
        listener = (Mylistener) getActivity();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = null;
                try {
                    input = read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int n = 0;
                //Toast.makeText(getActivity(), " "+input.length(), Toast.LENGTH_LONG).show();
                StringBuffer cur = new StringBuffer();
                Data []data = new Data[1000];
                for(int i = 0; i < input.length(); i++) {
                    char ch = input.charAt(i);
                    if(ch == '?')
                    {
                        if(data[n] == null) data[n] = new Data();
                        data[n].ques = cur.toString();
                        cur = new StringBuffer();
                    } else
                    if(ch == '!')
                    {
                        data[n].ans = cur.toString();
                        cur = new StringBuffer();
                        n++;
                    } else
                    {
                        if(ch != '\n') cur.append(ch);
                    }
                }

                StringBuffer output = new StringBuffer();
                for(int i = 0; i < n; i++)
                {
                    output.append(data[i].ques); output.append('?');
                    output.append(data[i].ans); output.append('!');
                    output.append("0 0 0");
                }
                try {
                    write(output.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity(), " "+input.length(), Toast.LENGTH_LONG).show();
            }
        });
        tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) listener.thank(1);
            }
        });
        //tasks.setText(xx+" ");
        return view;
    }
}
