package com.example.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class MainActivity extends AppCompatActivity{

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

    AlertDialog.Builder builder;

    //임산부 지원 사업 카드뉴스 이미지 버튼
    ImageButton home_1;
    ImageButton home_2;
    ImageButton home_3;
    ImageButton home_4;
    ImageButton home_5;
    ImageButton home_6;



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

        ChipNavigationBar.setItemSelected(R.id.home,true);

        home_1 = findViewById(R.id.home_1);
        home_2 = findViewById(R.id.home_2);
        home_3 = findViewById(R.id.home_3);
        home_4 = findViewById(R.id.home_4);
        home_5 = findViewById(R.id.home_5);
        home_6 = findViewById(R.id. home_6);

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
                qr.setOrientationLocked(false);  //세로로 스캔하기
                qr.setPrompt("임산부석 위에 부착된 QR코드를 찍어 착석을 인증해주세요");
                qr.initiateScan();

            }
        });

        //착석 취소버튼 누르면  ->  착석 인증 버튼 보임. 착성 취소버튼 동작하지 않음.
        CompletedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //정말로 착석 취소할지 물어보도록
                builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("착석 취소").setMessage("OK버튼을 누르면 착석이 취소됩니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "착석 취소됨", Toast.LENGTH_SHORT).show();
                        CompletedBtn.setVisibility(View.GONE);  //착석 취소버튼 안보이도록 수정
                        completedSeatnum.setText("");  //좌석 번호 반환됨
                        completedSeatnum.setVisibility(View.INVISIBLE);
                        QRBtn.setVisibility(View.VISIBLE);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "착석 유지됨", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

        home_1.setOnClickListener(new View.OnClickListener() { //임산부 건강 챙기기 카드뉴스 보여주기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easylaw.go.kr/CSP/EasyLawInfoR.laf?csmSeq=735&easySeq=2204&search_put=%EC%9E%84%EC%82%B0%EB%B6%80"));
                startActivity(intent);

            }
        });

        home_2.setOnClickListener(new View.OnClickListener() { //임산부 진료비 지원받기 카드뉴스 보여주기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easylaw.go.kr/CSP/EasyLawInfoR.laf?csmSeq=735&easySeq=2205&search_put=%EC%9E%84%EC%82%B0%EB%B6%80"));
                startActivity(intent);

            }
        });

        home_3.setOnClickListener(new View.OnClickListener() { //산모, 신생아 건강 관리사 지원 카드뉴스 보여주기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easylaw.go.kr/CSP/EasyLawInfoR.laf?csmSeq=735&easySeq=2207&search_put=%EC%9E%84%EC%82%B0%EB%B6%80"));
                startActivity(intent);

            }
        });

        home_4.setOnClickListener(new View.OnClickListener() { //출산비 등 지원받기 카드뉴스 보여주기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easylaw.go.kr/CSP/EasyLawInfoR.laf?csmSeq=735&easySeq=2206&search_put=%EC%9E%84%EC%82%B0%EB%B6%80"));
                startActivity(intent);

            }
        });

        home_5.setOnClickListener(new View.OnClickListener() { // 임산부 산전검사 및 난임지원 카드뉴스 보여주기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easylaw.go.kr/CSP/EasyLawInfoR.laf?csmSeq=735&easySeq=2203&search_put=%EC%9E%84%EC%82%B0%EB%B6%80"));
                startActivity(intent);

            }
        });

        home_6.setOnClickListener(new View.OnClickListener() { //임산기간 근로시간 단축제도 및 출산전후휴가 카드뉴스 보여주기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.easylaw.go.kr/CSP/EasyLawInfoR.laf?csmSeq=735&easySeq=2208&search_put=%EC%9E%84%EC%82%B0%EB%B6%80"));
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
                        intent.putExtra("name", name);
                        intent.putExtra("hospital", hospital); //설정 activity에 회원정보를 출력하기 위해 정보를 넘겨준다
                        startActivity(intent);
                        finish();
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