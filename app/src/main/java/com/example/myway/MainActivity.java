package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ImageButton QRBtn;
    ImageButton ChkEmptySeatBtn;
    ImageButton ChkPositionBtn;
    ImageButton SupportBtn;
    ChipNavigationBar ChipNavigationBar;
    String name;
    String hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QRBtn = findViewById(R.id.QRBtn);
        ChkEmptySeatBtn = findViewById(R.id.ChkEmptySeatBtn);
        ChkPositionBtn = findViewById(R.id.ChkPositionBtn);
        SupportBtn = findViewById(R.id.SupportBtn);
        ChipNavigationBar = findViewById(R.id.ChipNavigationBar);

        // 인텐트 전달받기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        hospital = intent.getStringExtra("hospital");


        QRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRScan.class);
                startActivity(intent);
            }
        });

        ChkEmptySeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EmptySeat.class);
                startActivity(intent);
            }
        });

        ChkPositionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FacilitiesLocationActiivty.class);
                startActivity(intent);
            }
        });

        SupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.seoulmomcare.com/main/main.do")); // 서울시 임산부 교통비 지원사업 홈페이지로 이동
                startActivity(intent);

            }
        });

        ChipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() { // 하단바를 통한 화면 이동코드
            @Override
            public void onItemSelected(int id) {

                switch(id){
                    case R.id.home:{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.subway:{
                        Intent intent = new Intent(getApplicationContext(), SubwayInfoActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.settings:{
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("hospital", hospital); //설정 activity에 회원정보를 출력하기 위해 정보를 넘겨준다
                        startActivity(intent);
                        break;
                    }
                }

            }
        });
    }

}