package com.example.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SubwayInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    ChipNavigationBar ChipNavigationBar;
    private MapView mapView;
    private static NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    ImageButton btnSearch;

    ArrayList<SubXYData> xydataArr = new ArrayList<SubXYData>();

    String afname;
    String geoquery;
    ImageView submap;
    ImageView refresh;

    TextView subname;
    EditText editSubwayName;
    ImageButton btnsearch;
    Marker marker = new Marker();

    String resultname;
    double resultx;
    double resulty;

    ArrayList<SubDataTime> dataArrTime;
    TextView upTextview1;
    TextView upTextview2;
    TextView dnTextview1;
    TextView dnTextview2;
    TextView TextdestUp;
    TextView TextdestDn;
    TextView destUp1;
    TextView destUp2;
    TextView destDn1;
    TextView destDn2;

    Integer upNum = 0;
    Integer dnNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_info);

        submap = findViewById(R.id.subinfo_submap);
        refresh = findViewById(R.id.subinfo_refresh);

        subname = findViewById(R.id.subinfo_subname);
        editSubwayName = findViewById(R.id.editSubwayName);
        btnsearch = findViewById(R.id.btnsearch);

        ChipNavigationBar = findViewById(R.id.ChipNavigationBar);
        mapView = (MapView) findViewById(R.id.subinfo_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        upTextview1 = findViewById(R.id.subinfo_upTextview1);
        upTextview2 = findViewById(R.id.subinfo_upTextview2);
        dnTextview1 = findViewById(R.id.subinfo_dnTextview1);
        dnTextview2 = findViewById(R.id.subinfo_dnTextview2);
        TextdestUp = findViewById(R.id.subinfo_TextdestUp);
        TextdestDn = findViewById(R.id.subinfo_TextdestDn);
        destUp1 = findViewById(R.id.subinfo_destUp1);
        destUp2 = findViewById(R.id.subinfo_destUp2);
        destDn1 = findViewById(R.id.subinfo_destDn1);
        destDn2 = findViewById(R.id.subinfo_destDn2);


        //지하철 도착 시간 갱신
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                targetURL = apiURL + editSubwayName.getText().toString();


                //검색되어 방향이 바뀌도록 코드 추가

                API api = new API();
                dataArrTime = api.parse();

                //api.t.start();
                Log.e("size", String.valueOf(dataArrTime.size()));
                for (SubDataTime data : dataArrTime) {
                    Log.e("data",data.getDest());//출력해보기  -> 출력 안 됨
                    Log.e("data",data.getWay());//출력해보기
                    //Log.e("data",data.getLine());//출력해보기
                    Log.e("data",data.getMinute());//출력해보기
                    //Log.e("data", data.getWay());

                    //호선 정보 서로 비교.
                    if(data.getLine().equals("1006")) {  //6호선만 가져옴

                        if (data.getWay().equals("상행") && upNum == 0) {
                            destUp1.setText(data.getDest().substring(0, data.getDest().indexOf("행")));  //종점 위치 받아오기
                            destUp2.setText(data.getDest().substring(0, data.getDest().indexOf("행")));  //도착정보가 없을 수 있으니까 종점 위치를 일단 설정하기 위해 추가함
                            TextdestUp.setText(data.getDest().substring(data.getDest().indexOf("-") + 2));
                            //upTextview1.setText(data.getMinute().substring(0, data.getDest().indexOf("후")));
                            if (data.getMinute().indexOf("후") != -1) {
                                upTextview1.setText(data.getMinute().substring(0, data.getMinute().indexOf("후")));
                            } else {
                                upTextview1.setText(data.getMinute());
                            }
                            //upTextview1.setText(data.getMinute());
                            upNum += 1;
                            //TextdestUp.setText(data.getDest());
                            //Log.e("data", "저기");
                            continue;
                        }
                        if (data.getWay().equals("하행") && dnNum == 0) {
                            destDn1.setText(data.getDest().substring(0, data.getDest().indexOf("행")));
                            destDn2.setText(data.getDest().substring(0, data.getDest().indexOf("행")));
                            TextdestDn.setText(data.getDest().substring(data.getDest().indexOf("-") + 2));
                            //dnTextview1.setText(data.getMinute().substring(0, data.getDest().indexOf("후")));
                            if (data.getMinute().indexOf("후") != -1) {
                                dnTextview1.setText(data.getMinute().substring(0, data.getMinute().indexOf("후")));
                            } else {
                                dnTextview1.setText(data.getMinute());
                            }
                            dnNum += 1;
                            //Log.e("data", "저기");
                            continue;
                        }
                        if (data.getWay().equals("상행") && upNum == 1) {
                            destUp2.setText(data.getDest().substring(0, data.getDest().indexOf("행")));  //종점 위치 받아오기
                            //TextdestUp.setText(data.getDest().substring(data.getDest().indexOf("-")+2));
                            //upTextview1.setText(data.getMinute().substring(0, data.getDest().indexOf("후")));
                            if (data.getMinute().indexOf("후") != -1) {
                                upTextview2.setText(data.getMinute().substring(0, data.getMinute().indexOf("후")));
                            } else {
                                upTextview2.setText(data.getMinute());
                            }
                            //upTextview2.setText(data.getMinute());
                            upNum += 1;
                            //TextdestUp.setText(data.getDest());
                            //Log.e("data", "저기");
                            continue;
                        }
                        if (data.getWay().equals("하행") && dnNum == 1) {
                            destDn2.setText(data.getDest().substring(0, data.getDest().indexOf("행")));
                            //TextdestDn.setText(data.getDest().substring(data.getDest().indexOf("-")+2));
                            //dnTextview1.setText(data.getMinute().substring(0, data.getDest().indexOf("후")));
                            if (data.getMinute().indexOf("후") != -1) {
                                dnTextview2.setText(data.getMinute().substring(0, data.getMinute().indexOf("후")));
                            } else {
                                dnTextview2.setText(data.getMinute());
                            }
                            //dnTextview2.setText(data.getMinute());
                            dnNum += 1;
                            //Log.e("data", "저기");
                            continue;
                        }
                    //}
                    //else{
                        //Toast.makeText(EmptySeat.this, "a몇 호선",Toast.LENGTH_SHORT);
                    }

                    //Log.e("data", "저기");
                }
            }
        });
        btnsearch.performClick();




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


        SubApiData apiData = new SubApiData();
        ArrayList<SubData> dataArr = apiData.getData();



        //위도 경도 값 리스트에 추가
        new Thread(()->{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder;

            for(SubData data : dataArr){
                try {
                    stringBuilder = new StringBuilder();
                    geoquery = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(data.getAdr(),"UTF-8");
                    URL geourl = new URL(geoquery);
                    HttpURLConnection conn = (HttpURLConnection) geourl.openConnection();
                    if(conn!=null){
                        conn.setConnectTimeout(5000);
                        conn.setReadTimeout(5000);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID","13tfim55ik");
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
                        double x = Double.parseDouble(stringBuilder.substring(indexFirst+5,indexLast));

                        indexFirst = stringBuilder.indexOf("\"y\":\"");
                        indexLast = stringBuilder.indexOf("\",\"distance\":");
                        double y = Double.parseDouble(stringBuilder.substring(indexFirst+5,indexLast));

                        xydataArr.add(new SubXYData(data.getName(),y,x));

                        bufferedReader.close();
                        conn.disconnect();
                    }
                } catch (UnsupportedEncodingException | MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < xydataArr.size(); i++) {
                    if (xydataArr.get(i).getName().indexOf("(") != -1) {
                        afname=xydataArr.get(i).getName().substring(0,xydataArr.get(i).getName().indexOf("("));
                    }else{
                        afname=xydataArr.get(i).getName();
                    }
                    if (editSubwayName.getText().toString().equals(xydataArr.get(i).getName())) {
                        resultname = xydataArr.get(i).getName();
                        resultx = xydataArr.get(i).getX();
                        resulty = xydataArr.get(i).getY();
                        setMarker(marker, resultx, resulty, R.drawable.ic_subinfo_marker,0);
                        break;
                    }
                }
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(resultx, resulty));
                naverMap.moveCamera(cameraUpdate);
            }
        });
    }


    public class SubXYData{
        String name;
        double x;
        double y;

        public SubXYData(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
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

    //지하철역 API 받아오기
    public class SubApiData{
        String key="$2a$10$IMtzc.n1u/g.L0xL28M/ueJt10zkC4ZDIhrwz8xLLBKl709PHJilq";
        String stationApiURL="https://openapi.kric.go.kr/openapi/convenientInfo/stationInfo?serviceKey=";
        String result = stationApiURL + key+ "&format=xml&railOprIsttCd=S5&lnCd=6";

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

                        String tagName="";
                        String name="",adr="";

                        //파싱
                        int eventType = parser.getEventType();
                        SubData data = new SubData();
                        while(eventType != XmlPullParser.END_DOCUMENT){
                            switch(eventType) {
                                case XmlPullParser.START_TAG:
                                    tagName = parser.getName();
                                    if (parser.getName().equals("item")) {
                                        data = new SubData();
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("item")) {
                                        dataArr.add(data);
                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    switch (tagName) {
                                        case "stinNm": {
                                            name = parser.getText();
                                            data.setName(name);
                                            break;
                                        }
                                        case "lonmAdr": {
                                            adr = parser.getText();
                                            data.setAdr(adr);
                                            break;
                                        }
                                    }
                                    break;
                            }
                            eventType = parser.next();

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
            try {
                t.start();
                t.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return dataArr;
        }

    }

    //
    public class SubDataTime {
        String way;  //상행, 하행
        String minute;  //몇 분 후 도착
        String line;   //몇 호선
        String dest;  //종점

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getLine() {
            return line;
        }

        public String getDest() {
            return dest;
        }

        public void setDest(String dest) {
            this.dest = dest;
        }
    }
    String apiURL = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/realtimeStationArrival/1/5/";
    String targetURL;

    public class API {
        public ArrayList<SubDataTime> parse() {
            ArrayList<SubDataTime> dataArr = new ArrayList<SubDataTime>();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(targetURL);
                        InputStream is = url.openStream();
                        XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = parserCreator.newPullParser();
                        parser.setInput(new InputStreamReader(is, "UTF-8"));
                        String tag = "";

                        //parser.next();

                        int parserEvent = parser.getEventType();

                        //Log.e("logcontinued","touched");
                        //SubData data = new SubData();
                        SubDataTime data = new SubDataTime();
                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            //Log.e("tag", "started");
                            //data = new SubData();
                            switch (parserEvent) {
                                /*case XmlPullParser.START_DOCUMENT:
                                    break;*/
                                case XmlPullParser.START_TAG:
                                    tag = parser.getName();
                                    if (tag.equals("row")) {
                                        data = new SubDataTime();
                                    }
                                    //break;
                                case XmlPullParser.END_TAG:
                                    if (tag.equals("row")) {
                                        dataArr.add(data);

                                        //Log.e("please", "잘 실행되길");//출력해보기
                                        Log.e("please", String.valueOf(dataArr.size()));

                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    switch (tag) {
                                        case "subwayId": {
                                            data.setLine(parser.getText());  //호선 입력받기
                                            //Log.e("data", data.getLine());//출력해보기
                                            break;
                                        }
                                        case "updnLine": {
                                            data.setWay(parser.getText());  //방향 입력받기
                                            //상행일 경우와 하행일 경우를 나눠서 넣기
                                            //Log.e("data", data.getWay());//출력해보기

                                            break;
                                        }
                                        case "trainLineNm": {
                                            data.setDest(parser.getText());  //어디 행인지 입력받기

                                            break;
                                        }
                                        case "arvlMsg2": {
                                            //도착 시간
                                            data.setMinute(parser.getText());

                                            break;
                                        }
                                    }

                            }

                            parserEvent = parser.next();

                        }
                    } catch (MalformedURLException | XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
            });

            t.start();
            try{
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*for (SubData data : dataArr) {
                Log.e("thread",data.getDest());//출력해보기  -> 출력 안 됨
                Log.e("thread",data.getWay());//출력해보기
                Log.e("thread",data.getLine());//출력해보기
                Log.e("thread",data.getMinute());//출력해보기
                //Log.e("data", data.getWay());
                if (data.getWay().equals("상행")) {
                    TextdestUp.setText(data.getDest());
                    //Log.e("data", "저기");
                }
                Log.e("data", "저기");
            }*/

            Log.e("final", String.valueOf(dataArr.size()));

            return dataArr;
        }
    }

    //마커 설정
    private void setMarker(Marker marker, double lat, double lng, int resourceID, int zIndex)
    {
        marker.setWidth(200);
        marker.setHeight(250);
        //원근감 표시
        marker.setIconPerspectiveEnabled(true);
        //아이콘 지정
        marker.setIcon(OverlayImage.fromResource(resourceID));
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
        this.naverMap = naverMap;
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