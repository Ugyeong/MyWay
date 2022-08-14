package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpSuccessActivity extends AppCompatActivity {

    Button BtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);

        BtnStart = findViewById(R.id.BtnStart);

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //시작하기 버튼 누르면 로그인 페이지로 이동하기
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}