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
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SubwayInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    ChipNavigationBar ChipNavigationBar;
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

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


        //지하철 노선도 액티비티로 이동
        submap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubwayMapActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ChipNavigationBar.setItemSelected(R.id.subway,true);  //네비게이션 바 지하철 선택되어 있도록 설정

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

        //API 마커 추가
        SubApiData apiData = new SubApiData();
        ArrayList<SubData> dataArr = apiData.getData();

        for(SubData data : dataArr){

        }



    }

    public class SubData{
        String name;
        String adr;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAdr() {
            return adr;
        }

        public void setAdr(String adr) {
            this.adr = adr;
        }

        @Override
        public String toString() {
            return "SubData{" +
                    "name='" + name + '\'' +
                    ", adr='" + adr + '\'' +
                    '}';
        }

    }

    public class SubApiData{
        String key="$2a$10$IMtzc.n1u/g.L0xL28M/ueJt10zkC4ZDIhrwz8xLLBKl709PHJilq";
        String stationApiURL="https://openapi.kric.go.kr/openapi/convenientInfo/stationInfo?serviceKey=";
        String result = stationApiURL + key+ "&format=xml&railOprIsttCd=S5&lnCd=5";

        public ArrayList<SubData> getData(){
            ArrayList<SubData> dataArr = new ArrayList<SubData>();
            Thread t = new Thread(){
                @Override
                public void run(){
                    try{
                        URL url = new URL(result);
                        InputStream is = url.openStream();

                        XmlPullParserFactory xmlFactory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = xmlFactory.newPullParser();
                        parser.setInput(is,"utf-8");

                        boolean sName=false, sAdr=false;
                        String name="", adr="";

                        //파싱
                        while(parser.getEventType() != XmlPullParser.END_DOCUMENT){
                            int type = parser.getEventType();
                            SubData data = new SubData();

                            if(type==XmlPullParser.START_TAG){
                                if(parser.getName().equals("item")){
                                    if(parser.getAttributeValue(0).equals("stinNm")){
                                        sName = true;
                                    }else if(parser.getAttributeValue(0).equals("lonmAdr")){
                                        sAdr = true;
                                    }
                                }
                            }

                            else if(type==XmlPullParser.TEXT){
                                if(sName){
                                    name = parser.getText();
                                    sName = false;
                                }else if(sAdr){
                                    adr = parser.getText();
                                    sAdr = false;
                                }
                            }

                            else if (type==XmlPullParser.END_TAG && parser.getName().equals("item")){
                                data.setName(name);
                                data.setAdr(adr);

                                dataArr.add(data);
                            }
                            type = parser.next();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }
                }
            };
            try{
                t.start();
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return dataArr;
        }

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