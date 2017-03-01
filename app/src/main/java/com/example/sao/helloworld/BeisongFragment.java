package com.example.sao.helloworld;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.os.health.PackageHealthStats;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sao on 2017/2/25.
 */

public class BeisongFragment extends Fragment implements View.OnClickListener{
    int n;


    public class Data{
        public String ques, ans;
        public int state, val, time;
    }
    Data []data = new Data[1000];
    @Nullable
    public String read() throws IOException {
        File file = new File("storage/emulated/0/wtf/data.txt");
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2024];
        int len = 0;
        len = fis.read(buffer);
        outstream.write(buffer, 0, len);
        Log.i("wtf2", outstream.toString());
        Log.i("wtf", " "+ len);
        fis.close(); outstream.close();
        return outstream.toString();
    }
    public void write(String output) throws IOException {
        File file = new File("storage/emulated/0/wtf/data.txt");
        FileOutputStream fis = new FileOutputStream(file);
        byte[] words = output.getBytes();
        fis.write(words);
        fis.close();
    }
    public void debug()
    {
        Log.i("wtf", "debug: bug");
    }

    public int getnumber(String str, int x, int ty)  //ty = 1 返回答案， ty = 0返回下一个数字的出现位置
    {
        int ans = 0, y = 0;
        boolean f = false;
        for(int i = x; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(f && (ch < '0' || ch > '9')) break;
            if(ch < '0' || ch > '9') continue;
            ans = ans*10 + ch - '0';
            y = i; f = true;
        }
        if(ty == 1) return ans;
        return y+1;
    }

    public void transtodata(String str)
    {
        n = 0;
        StringBuffer cur = new StringBuffer();
        for(int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(ch == '?')
            {
                if(data[n] == null) data[n] = new Data();
                data[n].ques = cur.toString();
                cur = new StringBuffer();
            } else if(ch == '!')
            {
                data[n].ans = cur.toString();
                cur = new StringBuffer();
                int x = i+1;
                data[n].state = getnumber(str, x, 1); x = getnumber(str, x, 0);
                data[n].val = getnumber(str, x, 1);   x = getnumber(str, x, 0);
                data[n].time = getnumber(str, x, 1);  x = getnumber(str, x, 0);
                n++;
                i = x;
            } else
            {
                cur.append(ch);
            }
        }
    }

    int next = 0;
    View view;
    TextView q, a;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.beisong, container, false);
        String input = " ";
        try {
            input = read();
        } catch (IOException e) {
            e.printStackTrace();
            debug();
        }
        Log.i("wtf", "wtfwtf");
        transtodata(input);
        q = (TextView) view.findViewById(R.id.Question);
        a = (TextView) view.findViewById(R.id.Answer);
        Button yes = (Button) view.findViewById(R.id.YES);
        Button no = (Button) view.findViewById(R.id.NO);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        yes.setText("YES");
        no.setText("NO");
        q.setText(data[next].ques);
        a.setText("");
        return view;
    }

    public void savedata()
    {
        StringBuffer output = new StringBuffer();
        for(int i = 0; i < n; i++)
        {
            output.append(data[i].ques); output.append('?');
            output.append(data[i].ans); output.append('!');
            output.append(data[i].state); output.append(" ");
            output.append(data[i].val); output.append(" ");
            output.append(data[i].time); output.append(" ");
        }
        try {
            write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modify(int i, int ty) {
        switch(ty)
        {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }

    public void onClick(View view) {
        if(next >= n) return;
        switch(view.getId())
        {
            case R.id.YES:
                modify(next, 4);
                next++;
                if(next >= n) return;
                q.setText(data[next].ques);
                a.setText("");
                break;
            case R.id.NO:
                modify(next, 1);
                a.setText(data[next].ans);
                break;
        }
        savedata();
    }

}
