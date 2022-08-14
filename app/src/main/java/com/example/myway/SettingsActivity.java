package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class SettingsActivity extends AppCompatActivity {

    ChipNavigationBar ChipNavigationBar;
    TextView TvName;
    TextView hospital_name;

    String name;
    String hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //인텐트 전달받기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        hospital = intent.getStringExtra("hospital");

        ChipNavigationBar = findViewById(R.id.ChipNavigationBar);

        TvName = findViewById(R.id.name);
        hospital_name = findViewById(R.id.hospital_name);

        //설정 액티비티에서 회원 정보 출력하기
        TvName.setText(name);
        hospital_name.setText(hospital);

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
                        startActivity(intent);
                        break;
                    }
                }

            }
        });
    }
}