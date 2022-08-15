package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.encoder.QRCode;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageButton QRBtn;
    ImageButton CompletedBtn;
    ImageButton ChkEmptySeatBtn;
    ImageButton ChkPositionBtn;
    ImageButton SupportBtn;
    ChipNavigationBar ChipNavigationBar;
    String name;
    String hospital;

    TextView seatDate;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    TextView seatNum;
    String seatnum;
    BottomSheetDialog bottomSheetDialog;
    View view;
    IntentIntegrator qr;
    ImageButton btnOk;
    ImageButton btnCancel;
    TextView completedSeatnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QRBtn = findViewById(R.id.QRBtn);
        CompletedBtn = findViewById(R.id.CompletedBtn);
        ChkEmptySeatBtn = findViewById(R.id.ChkEmptySeatBtn);
        ChkPositionBtn = findViewById(R.id.ChkPositionBtn);
        SupportBtn = findViewById(R.id.SupportBtn);
        ChipNavigationBar = findViewById(R.id.ChipNavigationBar);
        completedSeatnum = findViewById(R.id.completedSeatnum);

        // 인텐트 전달받기
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        hospital = intent.getStringExtra("hospital");

        //바텀시트 설정
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bottomsheet_seatnum, null, false);
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(view);
        //바텀시트 버튼 동작
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //좌석 명 전달, 착석 인증되도록 버튼 변경
                bottomSheetDialog.dismiss();
                QRBtn.setVisibility(View.GONE);  //착석 인증 버튼이 동작하지 않도록 변경
                CompletedBtn.setVisibility(View.VISIBLE);  //착석 취소버튼 보이도록 수정
                completedSeatnum.setText(seatnum);  //QR에서 받아온 좌석 번호로 설정
                completedSeatnum.setVisibility(View.VISIBLE);
            }
        });

        QRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), QRScan.class);
                //startActivity(intent);
                //qr인증 카메라 뜸
                qr = new IntentIntegrator(MainActivity.this);
                qr.setOrientationLocked(false);  //세로로 스캔하기  동작 안되는 중???????
                qr.setPrompt("임산부석 위에 부착된 QR코드를 찍어 착석을 인증해주세요");
                qr.initiateScan();

            }
        });

        //착석 취소버튼 누르면  ->  착석 인증 버튼 보임. 착성 취소버튼 동작하지 않음.
        CompletedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompletedBtn.setVisibility(View.GONE);  //착석 취소버튼 안보이도록 수정
                completedSeatnum.setText("");  //좌석 번호 반환됨
                completedSeatnum.setVisibility(View.INVISIBLE);
                QRBtn.setVisibility(View.VISIBLE);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        seatNum = view.findViewById(R.id.seatNum);

        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this, "잘못된 QR코드입니다.", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                //일시 받아오기
                seatDate = view.findViewById(R.id.seatDate);
                mNow = System.currentTimeMillis();
                mDate = new Date(mNow);
                seatDate.setText(mFormat.format(mDate));
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    seatnum=obj.getString("seatNum");  //QR코드에서 좌석 번호 얻어오기
                    seatNum.setText(seatnum);
                    bottomSheetDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    //textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}