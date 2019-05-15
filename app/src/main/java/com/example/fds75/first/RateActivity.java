package com.example.fds75.first;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate = "";

    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = (EditText) findViewById(R.id.rmb);
        show = (TextView) findViewById(R.id.showOut);

        //获取SharedPreference里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        //另：SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);//"0.0f"为默认值
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);//第一次运行取此处的值
        updateDate = sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//将时间转换成字符串
        final String todayStr = sdf.format(today);

        Log.i(TAG,"onCreate:sp dollarRate="+dollarRate);
        Log.i(TAG,"onCreate:sp euroRate="+euroRate);
        Log.i(TAG,"onCreate:sp wonRate="+wonRate);
        Log.i(TAG,"onCreate:sp updateDate="+updateDate);

        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG,"需要更新");
            //开启子线程
            Thread t = new Thread(this);//在此处需加上当前对象
            t.start();
        }else {
            Log.i(TAG,"不需要更新");
        }

        handler = new Handler(){//改写父类方法
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){//观察是否与run方法中的what相同
                    //String str = (String) msg.obj;
                    Bundle bdl = (Bundle)msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");
//                    Log.i(TAG,"handleMessage:getMessage msg = "+str);
//                    show.setText(str);//将输出内容通过show呈现出来

                    Log.i(TAG,"handleMessage:dollarRate:"+dollarRate);
                    Log.i(TAG,"handleMessage:euroRate:"+euroRate);
                    Log.i(TAG,"handleMessage:wonRate:"+wonRate);

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();//保存数据

                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    }

    public void onClick(View btn){
        //获取用户输入内容
        String str = rmb.getText().toString();
        float r=0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            //提示用户输入
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        if(btn.getId()==R.id.btn_dollar){
            show.setText(String.format("%.2f",r*dollarRate));
        }else if(btn.getId()==R.id.btn_euro){
            show.setText(String.format("%.2f",r*euroRate));
        }else {
            show.setText(String.format("%.2f",r*wonRate));
        }
        //        float val;
//        if(btn.getId()==R.id.btn_dollar){
//            val = r * (1/6.7f);
//            //show.setText(String.format("%.2f",r*dollarRate));
//        }else if(btn.getId()==R.id.btn_euro){
//            val = r * (1/11f);
//        }else {
//            val = r * 500;
//        }
//        show.setText(String.valueOf(val));

    }


    public void openOne(View btn){
        //打开一个activity
        Log.i("open","openOne");
        openConfig();
//        //用intent对象传参数
//        Intent config = new Intent(this,ConfigActivity.class);
////        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
////        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
//        config.putExtra("dollar_rate_key",dollarRate);//调用方法，传到下个页面
//        config.putExtra("euro_rate_key",euroRate);
//        config.putExtra("won_rate_key",wonRate);
//
//        Log.i(TAG,"openOne:dollarRate="+dollarRate);
//        Log.i(TAG,"openOne:euroRate="+euroRate);
//        Log.i(TAG,"openOne:wonRate="+wonRate);


    }

    private void openConfig() {
        //用intent对象传参数
        Intent config = new Intent(this,ConfigActivity.class);
//        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        config.putExtra("dollar_rate_key",dollarRate);//调用方法，传到下个页面
        config.putExtra("euro_rate_key",euroRate);
        config.putExtra("won_rate_key",wonRate);

        Log.i(TAG,"openOne:dollarRate="+dollarRate);
        Log.i(TAG,"openOne:euroRate="+euroRate);
        Log.i(TAG,"openOne:wonRate="+wonRate);
        //startActivity(config);
        startActivityForResult(config,1);//表示打开这个窗口是为了带回数据,"1"是请求代码
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);    //将资源加载到menu里面
        return true;
    }

    //按菜单项时做出处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //通过id区分不同的菜单项
        if (item.getItemId()==R.id.menu_set){
            //将原代码提取成方法引入
            openConfig();
        }else if (item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent List = new Intent(this,MyList2Activity.class);
            startActivity(List);
        }

        return super.onOptionsItemSelected(item);
    }

    //处理带回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //requestCode区分是谁返回的数据，resultCode区分返回的数据按什么格式拆分
        if(requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);//在bundle中获取新的值
            euroRate = bundle.getFloat("key_euro",0.1f);//Bundle中默认值为0.1f，intent中为0.0f
            wonRate = bundle.getFloat("key_won",0.1f);

            Log.i(TAG,"onAcitivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onAcitivityResult:euroRate="+euroRate);
            Log.i(TAG,"onAcitivityResult:wonRate="+wonRate);

            //将新设置的汇率写到SharedPreference里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据以保存到SharedPreferences");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run...");
//        for(int i=1;i<6;i++){//加上一个空循环做延时操作
//            Log.i(TAG,"run:i"+i);
//            try {//由于此方法容易产生异常，就需捕获它
//                Thread.sleep(2000);//让这个线程停止两秒
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        //保存获取的汇率
        Bundle bundle;

//        //获取网络数据
//        URL url = null;
//        try {
//            url = new URL("http://www.usd-cny.com/bankofchina.htm");//捕获异常
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();//获得一个输入流
//
//            String html = inputStream2String(in);//将输入流转换成字符串
//            Log.i(TAG,"run:html"+html);
//
//            Document doc = Jsoup.parse(html);//接收数据
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        bundle = getFromBOC();

        //bundle中保存所获取的汇率

        //获取message对象，用于返回主线程
        Message msg = handler.obtainMessage();
        //msg.what = 5;//what用于标记当前message的属性，用于整数
        //Message mag = handler.obtainMessage(5); 上面两排可直接通过定义what来写
        //msg.obj = "Hello from run()";
        msg.obj=bundle;
        handler.sendMessage(msg);//将msg发送到队列里

    }

    //从bankofchina获取数据
    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            //doc = Jsoup.parse(html);//接收数据
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
//            int i=1;
//            for(Element table : tables){//找寻是哪一个table，找到后可删除
//                Log.i(TAG,"run:table["+i+"]="+table);
//                i++;
//            }

            Element table2 = tables.get(1);//提取table2
            Log.i(TAG,"run:table2="+table2);
            //获取TD中的数据
            Elements tds = table2.getElementsByTag("td");
            for (int j=0;j<tds.size();j+=8){
                Element td1 = tds.get(j);//提取币种名称
                Element td2 = tds.get(j+5);//提取货币汇率
                Log.i(TAG,"run:"+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if ("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    /*private Bundle getFromUsdCny() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);//接收数据
            Log.i(TAG,"run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
//            int i=1;
//            for(Element table : tables){//找寻是哪一个table，找到后可删除
//                Log.i(TAG,"run:table["+i+"]="+table);
//                i++;
//            }

            Element table6 = tables.get(5);//提取table6
            Log.i(TAG,"run:table6="+table6);
            //获取TD中的数据
            Elements tds = table6.getElementsByTag("td");
            for (int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);//提取币种名称
                Element td2 = tds.get(i+5);//提取货币汇率
                Log.i(TAG,"run:"+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();

                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if ("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }
*/
    private String inputStream2String(InputStream inputStream) throws IOException {//将输入流转换成字符串输出
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");//出现异常抛出
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);//出现异常抛出
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
