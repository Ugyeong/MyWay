package com.example.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SubwayInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    ChipNavigationBar ChipNavigationBar;
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    String key="$2a$10$IMtzc.n1u/g.L0xL28M/ueJt10zkC4ZDIhrwz8xLLBKl709PHJilq";
    String stationApiURL="https://openapi.kric.go.kr/openapi/convenientInfo/stationInfo?serviceKey=";
    String result = stationApiURL + key+ "&format=xml&railOprIsttCd=S5&lnCd=5";

    ImageView submap;
    ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_info);

        submap = findViewById(R.id.subinfo_submap);
        refresh = findViewById(R.id.subinfo_refresh);

        ChipNavigationBar = findViewById(R.id.ChipNavigationBar);
        mapView = (MapView) findViewById(R.id.subinfo_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        try {
            URL url = new URL(result);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //지하철 노선도 액티비티로 이동
        submap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubwayMapActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //해당 액티비티 새로고침
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        //하단바 설정
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

    //지도 설정
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}