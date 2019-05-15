package com.zhihu.matisse.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btnMatisser = findViewById(R.id.btn_matisser);
        Button btnMatisse = findViewById(R.id.btn_matisse);

        btnMatisser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,MatisserSampleActivity.class);
                startActivity(intent);
            }
        });

        btnMatisse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,MatisseSampleActivity.class);
                startActivity(intent);
            }
        });
    }
}
