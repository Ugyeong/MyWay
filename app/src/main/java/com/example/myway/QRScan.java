package com.example.myway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QRScan extends AppCompatActivity{

    SurfaceView QRScan;
    View view;
    Button btnCheck;
    IntentIntegrator qr;
    ImageButton btnOk;
    ImageButton btnCancel;
    ImageView btnPrev;
    TextView seatDate;
    TextView seatNum;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    String seatnum;

    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        QRScan = findViewById(R.id.surfaceView);

        qr = new IntentIntegrator(this);
        qr.setOrientationLocked(false);  //세로로 스캔하기
        qr.initiateScan();

        btnCheck = findViewById(R.id.btn_check);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.bottomsheet_seatnum, null, false);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        seatDate = view.findViewById(R.id.seatDate);
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        seatDate.setText(mFormat.format(mDate));

        btnCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //bottomSheetDialog.
                //일시 기록
                mNow = System.currentTimeMillis();
                mDate = new Date(mNow);
                seatDate.setText(mFormat.format(mDate));
                bottomSheetDialog.show();
            }
        });
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
                //메인 액티비티로 이동 (좌석 명 전달하기)
                bottomSheetDialog.dismiss();
            }
        });
        btnPrev = findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
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
                //Toast.makeText(QRScan.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                //Toast.makeText(QRScan.this, "스캔완료!", Toast.LENGTH_SHORT).show();
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