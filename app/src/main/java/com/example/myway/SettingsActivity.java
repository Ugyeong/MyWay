package com.example.myway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class SettingsActivity extends AppCompatActivity {

    ChipNavigationBar ChipNavigationBar;
    TextView TvName;
    TextView hospital_name;

    String name;
    String hospital;

    ImageButton btn_rule_set;
    ImageButton btn_help_set;
    ImageButton btn_logout_set;
    TextView versionText;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_rule_set = findViewById(R.id.btn_rule_set);
        btn_help_set = findViewById(R.id.btn_help_set);
        btn_logout_set = findViewById(R.id.btn_logout_set);
        versionText = findViewById(R.id.versionText);

        btn_rule_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(SettingsActivity.this);

                builder.setTitle("위치기반서비스 이용약관").setMessage(R.string.rule);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btn_logout_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(SettingsActivity.this);
                dlg.setTitle("로그아웃");
                dlg.setMessage("로그아웃 하시겠습니까?");
                dlg.setIcon(R.drawable.app_icon_my);

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear(); //저장되어 있는 값들을 지운다
                        editor.commit();

                        finish();
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { // 취소버튼 누르면 아무것도 동작시키지 않음

                    }
                });

               dlg.show();

            }
        });

        //인텐트 전달받기
        //Intent intent = getIntent();
        //name = intent.getStringExtra("name");
        //hospital = intent.getStringExtra("hospital");

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
        name = sharedPreferences.getString("name", null);
        hospital = sharedPreferences.getString("hospital",null);

        ChipNavigationBar = findViewById(R.id.ChipNavigationBar);

        TvName = findViewById(R.id.name);
        hospital_name = findViewById(R.id.hospital_name);

        //설정 액티비티에서 회원 정보 출력하기
        TvName.setText(name);
        hospital_name.setText(hospital);

        ChipNavigationBar.setItemSelected(R.id.settings,true);  //네비게이션 바 세팅 선택되어 있도록 설정

        ChipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() { // 하단바를 통한 화면 이동코드
            @Override
            public void onItemSelected(int id) {

                switch(id){
                    case R.id.home:{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    case R.id.subway:{
                        Intent intent = new Intent(getApplicationContext(), SubwayInfoActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }

                    case R.id.settings:{
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

            }
        });
    }
}