package com.example.fds75.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    private final String TAG = "ConfigActivity" ;

    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();//获得当前的intent对象
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);//获取数据
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        Log.i("cfg","onCreat:dollar2="+dollar2);
        Log.i("cfg","onCreat:euro2="+euro2);
        Log.i("cfg","onCreat:won2="+won2);

        dollarText=(EditText)findViewById(R.id.dollar_rate);
        euroText=(EditText)findViewById(R.id.euro_rate);
        wonText=(EditText)findViewById(R.id.won_rate);

        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));
    }

    public void save(View btn) {
        Log.i(TAG,"save:");
        //获取新的值
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());
        Log.i(TAG,"save:获取到新的值");
        Log.i("cfg","save:newDollar="+newDollar);
        Log.i("cfg","save:newEuro="+newEuro);
        Log.i("cfg","save:newWon="+newWon);

        //保存到Bundle或放入到Extra
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);//这里的key可以与上面相同，也可不同
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);//打包带回
        setResult(2,intent);//“2”是相应代码

        //返回到调用页面
        finish();
    }
}
