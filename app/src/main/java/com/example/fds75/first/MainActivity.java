package com.example.fds75.first;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        out = findViewById(R.id.txtout);
        edit = findViewById(R.id.inp);
        //out.setText(R.string.hint_str);

        EditText inp = findViewById(R.id.inp);
        int len = edit.length();
        if(edit.length() == 0);
        Toast.makeText(this, "请输入", Toast.LENGTH_SHORT).show();

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
       // TextView out = findViewById(R.id.txtout);
        String str = edit.getText().toString();
        double doubleStr=Double.valueOf(str);
        double f;
        f=(double) (32+doubleStr*1.8);

        out.setText("结果为:"+String.format("%.2f", f)+"华氏度");
    }

}
