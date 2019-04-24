package com.example.serg.clock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Setting extends AppCompatActivity {
    int time_perenos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button save = findViewById(R.id.save);
        final EditText time_perenos = findViewById(R.id.int_time);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d;
                if(!time_perenos.getText().toString().equals("")) {
                     d = Integer.parseInt(time_perenos.getText().toString());
                }else{
                     d = 5;
                }
                Intent intent = new Intent(Setting.this, MainActivity.class);
                intent.putExtra("Time_perenos", d);
                startActivity(intent);
            }
        });
    }

}
