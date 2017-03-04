package com.example.sao.helloworld;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.Date;

/**
 * Created by sao on 2017/2/25.
 */

public class CountryFragment extends Fragment {
    public int[] admintime_1 = {10, 20, 30, 60, 120},
            admintime_2 = {120, 240, 240, 240, 300},
            admintime_3 = {43200, 86400, 86400, 172800, 172800},
            admintime_4 = {604800, 604800, 1209600, 2419200, 2419200};
    long[] time_1 = new long[5], time_2 = new long[5], time_3 = new long[5], time_4 = new long[5];
    int nlearn = 0;
    public class Data{
        public String ques, ans;
        public int flag;
        public int level, val;
        public long time, begintime;
        //flag level val time begintime
    }
    Data[] data = new Data[2017];
    String learndata = null;
    File learnfile;
    public int max(int a, int b) { return a < b ? b : a; }
    public int min(int a, int b) { return a > b ? b : a; }


    //与activity进行通信
    public Mylistener listener;
    public interface Mylistener
    {
        public void countrysend(int x, String str);
        public void memorysend(String str);
    }

    //将output写入文件wtf
    public void write(String output) throws IOException {
        File path = new File("storage/emulated/0/wtf");// TODO: 2017/3/2
        File file = new File(path, "data.txt");
        //if(!file.exists())Toast.makeText(getActivity(), Environment.getExternalStorageDirectory().getPath(), Toast.LENGTH_LONG).show();
        FileOutputStream fis = new FileOutputStream(file);
        byte[] words = output.getBytes();
        fis.write(words);
        fis.close();
    }
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
    //将数组转换成string
    public String numarraytostr(int []a)
    {
        StringBuffer temp = new StringBuffer();
        for(int i = 0; i < a.length; i++) temp.append(a[i]+" "); temp.append("\n");
        return temp.toString();
    }

    //获得知识点等级需要的间隔时间
    public long getleveltime(int k)
    {
        if(k <= 5) return time_1[k-1];
        if(k <= 10) return time_2[k-6];
        if(k <= 15) return time_3[k-11];
        if(k <= 20) return time_4[k-16];
        return (long)1e12;
    }

    //从字符串获得从x位置的第一个数字  ty = 1 返回答案， ty = 0返回下一个数字的出现位置
    public long getnumber(String str, int x, int ty)
    {
        long ans = 0, y = 0;
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

    //将str转化为data
    public int transtodata(String str)
    {
        int n = 0;
        StringBuffer cur = new StringBuffer();
        int start = 0;
        for(int i = 0; i < 5; i++) { time_1[i] = getnumber(str, start, 1); start = (int)getnumber(str, start, 0); }
        for(int i = 0; i < 5; i++) { time_2[i] = getnumber(str, start, 1); start = (int)getnumber(str, start, 0); }
        for(int i = 0; i < 5; i++) { time_3[i] = getnumber(str, start, 1); start = (int)getnumber(str, start, 0); }
        for(int i = 0; i < 5; i++) { time_4[i] = getnumber(str, start, 1); start = (int)getnumber(str, start, 0); }
        while(str.charAt(start) != '#') start++;
        for(int i = start+1; i < str.length(); i++)
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
                data[n].flag = (int)getnumber(str, x, 1); x = (int) getnumber(str, x, 0);
                data[n].level = (int)getnumber(str, x, 1); x = (int)getnumber(str, x, 0);
                data[n].val = (int)getnumber(str, x, 1);   x = (int)getnumber(str, x, 0);
                data[n].time = getnumber(str, x, 1);  x = (int)getnumber(str, x, 0);
                data[n].begintime = getnumber(str, x, 1); x = (int)getnumber(str, x, 0);
                n++;
                i = x;
            } else
            {
                cur.append(ch);
            }
        }
        return n;
    }

    //通过滑动条获取单词量
    TextView barword;
    public class Wordnumlistener implements SeekBar.OnSeekBarChangeListener
    {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            barword.setText(("今日要背诵知识量："+i/10+"个"));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    //layout布局设置
    View view;
    Button tasks, download, memory;
    TextView totalword, classword;
    public void layoutinit()
    {
        listener = (Mylistener)getActivity();
        tasks = (Button) view.findViewById(R.id.todaytask);
        download = (Button) view.findViewById(R.id.download);
        memory = (Button) view.findViewById(R.id.memoryfunction);
    }

    //切换到memoryline页面
    public class Memorylistener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            listener.memorysend(learndata);
        }
    }

    //分析当前数据  获得学习单词量
    public void analysedata(File file)
    {
        String input = "";
        try {
            input = read(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n = transtodata(input), n1, n2, n3, n4, newn; newn = nlearn = n1 = n2 = n3 = n4 = 0;
        for(int i = 0; i < n; i++)
        {
            if(data[i].level <= 5) n1++;
            else if(data[i].level <= 10) n2++;
            else if(data[i].level <= 15) n3++;
            else n4++;
            if(data[i].time + getleveltime(data[i].level) < new Date().getTime()/1000) nlearn++;
            if(data[i].flag == 0) newn++;
        }
        totalword = (TextView) view.findViewById(R.id.totalword);
        classword = (TextView) view.findViewById(R.id.classword);
        totalword.setText(("复习量/知识点总量:  " + nlearn + "/" + n + "   新知识点:" + newn));
        classword.setText(("陌生:"+n1 + "  有印象:" + n2 + "  比较熟悉:" + n3 + "  非常熟悉:" + n4));

        barword = (TextView) view.findViewById(R.id.countrybar);
        barword.setText(("今日要背诵知识量："+0+"个"));
        SeekBar seekbar = (SeekBar) view.findViewById(R.id.beisongbar);
        seekbar.setMax((n - nlearn)*10);
        seekbar.setOnSeekBarChangeListener(new Wordnumlistener());
    }

    //导入新数据
    public class Downloadlistener implements  View.OnClickListener
    {
        @Override
        public void onClick(View view) {

        }
    }

    //进入新的任务
    public class Taskslistener implements  View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            int x = (int)getnumber(barword.getText().toString(), 0, 1);
            if(x+nlearn <= 0) { Toast.makeText(getActivity(), "？？？0个单词怎么背", Toast.LENGTH_LONG).show(); return; }
            if(listener != null) listener.countrysend(x, learndata);
        }
    }
    String ESD = "storage/emulated/0/wtf/";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentcountry, container, false);
        layoutinit();
        learndata = getArguments().get("data")+"";
        learnfile = new File(ESD + learndata +".txt");
        TextView wtf = (TextView) view.findViewById(R.id.textView);
        wtf.setText(learndata);
        analysedata(learnfile);
        download.setOnClickListener(new Downloadlistener());
        memory.setOnClickListener(new Memorylistener());
        tasks.setOnClickListener(new Taskslistener());
        return view;
    }
}
