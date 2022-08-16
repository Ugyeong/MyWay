package com.example.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SubwayInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    ChipNavigationBar ChipNavigationBar;
    private MapView mapView;
    private static NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    String geoquery;
    Marker marker = new Marker();
    ImageView submap;
    ImageView refresh;

    TextView subname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_info);

        submap = findViewById(R.id.subinfo_submap);
        refresh = findViewById(R.id.subinfo_refresh);

        subname = findViewById(R.id.subinfo_subname);

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

        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();

        for(SubData data : dataArr){
            try {
                geoquery = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(data.getAdr(),"UTF-8");
                URL geourl = new URL(geoquery);
                HttpURLConnection conn = (HttpURLConnection) geourl.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID\t","13tfim55ik");
                    conn.setRequestProperty("X-NCP-APIGW-API-KEY","x0xNEmBmdoSN0iO4HLywQuoH2WNydnzZOxVhUG6Y");
                    conn.setDoInput(true);

                    int responseCode = conn.getResponseCode();
                    if(responseCode ==200){
                        bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    }else{
                        bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }

                    String line = null;
                    while((line = bufferedReader.readLine())!=null){
                        stringBuilder.append(line+"\n");
                    }

                    int indexFirst;
                    int indexLast;

                    indexFirst = stringBuilder.indexOf("\"x\":\"");
                    indexLast = stringBuilder.indexOf("\",\"y\":");
                    String x = stringBuilder.substring(indexFirst+5,indexLast);
                    //double x = Double.parseDouble(stringBuilder.substring(indexFirst+5,indexLast));

                    indexFirst = stringBuilder.indexOf("\"y\":\"");
                    indexLast = stringBuilder.indexOf("\",\"distance\":");
                    double y = Double.parseDouble(stringBuilder.substring(indexFirst+5,indexLast));

                    Toast.makeText(getApplicationContext(), x, Toast.LENGTH_SHORT).show();
                    //setMarker(marker,x,y,R.drawable.ic_subinfo_marker,0);

                    bufferedReader.close();
                    conn.disconnect();
                }
            } catch (UnsupportedEncodingException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        parser.setInput(new InputStreamReader(is, "UTF-8"));

                        String name="", adr="";
                        String tag="";

                        //파싱
                        parser.next();
                        int type = parser.getEventType();
                        while(type != XmlPullParser.END_DOCUMENT){
                            SubData data = new SubData();
                            if(type==XmlPullParser.START_TAG){
                                tag=parser.getName();
                                if(tag.equals("item"));
                                else if(tag.equals("stinNm")){
                                    parser.next();
                                    data.setName(parser.getText());
                                }
                                else if(tag.equals("lonmAdr")){
                                    parser.next();
                                    data.setAdr(parser.getText());
                                }
                            }
                            dataArr.add(data);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e){
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

    //마커 설정
    private void setMarker(Marker marker, double lat, double lng, int resourceID, int zIndex)
    {
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        //아이콘 지정
        marker.setIcon(OverlayImage.fromResource(resourceID));
        //마커의 투명도
        marker.setAlpha(0.8f);
        //마커 위치
        marker.setPosition(new LatLng(lat, lng));
        //마커 우선순위
        marker.setZIndex(zIndex);
        //마커 표시
        marker.setMap(naverMap);
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