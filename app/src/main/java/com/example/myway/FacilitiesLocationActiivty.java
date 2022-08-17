package com.example.myway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
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


public class FacilitiesLocationActiivty extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    ArrayList<SubXYData> xydataArr = new ArrayList<SubXYData>();

    String geoquery;
    String afname;
    String resultname;
    double resultx;
    double resulty;

    ImageView btnPrev;
    ImageView myLocation;

    EditText editSubwayName;
    ImageButton btnsearch;

    Marker nr_marker1 = new Marker();
    Marker nr_marker2 = new Marker();
    Marker nr_marker3 = new Marker();
    Marker nr_marker4 = new Marker();
    Marker nr_marker5 = new Marker();
    Marker nr_marker6 = new Marker();
    Marker nr_marker7 = new Marker();
    Marker nr_marker8 = new Marker();
    Marker nr_marker9 = new Marker();
    Marker nr_marker10 = new Marker();
    Marker nr_marker11 = new Marker();
    Marker nr_marker12 = new Marker();
    Marker nr_marker13 = new Marker();
    Marker nr_marker14 = new Marker();
    Marker nr_marker15 = new Marker();
    Marker nr_marker16 = new Marker();
    Marker nr_marker17 = new Marker();
    Marker nr_marker18 = new Marker();
    Marker nr_marker19 = new Marker();
    Marker nr_marker20 = new Marker();
    Marker nr_marker21 = new Marker();
    Marker nr_marker22 = new Marker();
    Marker nr_marker23 = new Marker();
    Marker nr_marker24 = new Marker();
    Marker nr_marker25 = new Marker();
    Marker nr_marker26 = new Marker();
    Marker nr_marker27 = new Marker();
    Marker nr_marker28 = new Marker();
    Marker nr_marker29 = new Marker();
    Marker nr_marker30 = new Marker();
    Marker nr_marker31 = new Marker();
    Marker nr_marker32 = new Marker();
    Marker nr_marker33 = new Marker();
    Marker nr_marker34 = new Marker();
    Marker nr_marker35 = new Marker();
    Marker nr_marker36 = new Marker();
    Marker nr_marker37 = new Marker();
    Marker nr_marker38 = new Marker();
    Marker nr_marker39 = new Marker();
    Marker nr_marker40 = new Marker();
    Marker nr_marker41 = new Marker();
    Marker nr_marker42 = new Marker();
    Marker nr_marker43 = new Marker();
    Marker nr_marker44 = new Marker();
    Marker nr_marker45 = new Marker();
    Marker nr_marker46 = new Marker();
    Marker nr_marker47 = new Marker();
    Marker nr_marker48 = new Marker();
    Marker nr_marker49 = new Marker();
    Marker nr_marker50 = new Marker();
    Marker nr_marker51 = new Marker();
    Marker nr_marker52 = new Marker();
    Marker nr_marker53 = new Marker();
    Marker nr_marker54 = new Marker();
    Marker nr_marker55 = new Marker();
    Marker nr_marker56 = new Marker();
    Marker nr_marker57 = new Marker();
    Marker nr_marker58 = new Marker();
    Marker nr_marker59 = new Marker();
    Marker nr_marker60 = new Marker();
    Marker nr_marker61 = new Marker();
    Marker nr_marker62 = new Marker();
    Marker nr_marker63 = new Marker();
    Marker nr_marker64 = new Marker();
    Marker nr_marker65 = new Marker();
    Marker nr_marker66 = new Marker();
    Marker nr_marker67 = new Marker();
    Marker nr_marker68 = new Marker();
    Marker nr_marker69 = new Marker();
    Marker nr_marker70 = new Marker();
    Marker nr_marker71 = new Marker();
    Marker nr_marker72 = new Marker();
    Marker nr_marker73 = new Marker();
    Marker nr_marker74 = new Marker();
    Marker nr_marker75 = new Marker();
    Marker nr_marker76 = new Marker();
    Marker nr_marker77 = new Marker();
    Marker nr_marker78 = new Marker();
    Marker nr_marker79 = new Marker();
    Marker nr_marker80 = new Marker();
    Marker nr_marker81 = new Marker();
    Marker nr_marker82 = new Marker();
    Marker nr_marker83 = new Marker();
    Marker nr_marker84 = new Marker();
    Marker nr_marker85 = new Marker();
    Marker nr_marker86 = new Marker();
    Marker nr_marker87 = new Marker();
    Marker nr_marker88 = new Marker();
    Marker nr_marker89 = new Marker();
    Marker nr_marker90 = new Marker();
    Marker nr_marker91 = new Marker();
    Marker nr_marker92 = new Marker();
    Marker nr_marker93 = new Marker();
    Marker nr_marker94 = new Marker();
    Marker nr_marker95 = new Marker();
    Marker nr_marker96 = new Marker();
    Marker nr_marker97 = new Marker();

    Marker el_marker1 = new Marker();
    Marker el_marker2 = new Marker();
    Marker el_marker3 = new Marker();
    Marker el_marker4 = new Marker();
    Marker el_marker5 = new Marker();
    Marker el_marker6 = new Marker();
    Marker el_marker7 = new Marker();
    Marker el_marker8 = new Marker();
    Marker el_marker9 = new Marker();
    Marker el_marker10 = new Marker();
    Marker el_marker11 = new Marker();
    Marker el_marker12 = new Marker();
    Marker el_marker13 = new Marker();
    Marker el_marker14 = new Marker();
    Marker el_marker15 = new Marker();
    Marker el_marker16 = new Marker();
    Marker el_marker17 = new Marker();
    Marker el_marker18 = new Marker();
    Marker el_marker19 = new Marker();
    Marker el_marker20 = new Marker();
    Marker el_marker21 = new Marker();

    Marker es_marker1 = new Marker();
    Marker es_marker2 = new Marker();
    Marker es_marker3 = new Marker();
    Marker es_marker4 = new Marker();
    Marker es_marker5 = new Marker();
    Marker es_marker6 = new Marker();
    Marker es_marker7 = new Marker();
    Marker es_marker8 = new Marker();
    Marker es_marker9 = new Marker();
    Marker es_marker10 = new Marker();
    Marker es_marker11 = new Marker();
    Marker es_marker12 = new Marker();
    Marker es_marker13 = new Marker();
    Marker es_marker14 = new Marker();
    Marker es_marker15 = new Marker();
    Marker es_marker16 = new Marker();
    Marker es_marker17 = new Marker();
    Marker es_marker18 = new Marker();
    Marker es_marker19 = new Marker();
    Marker es_marker20 = new Marker();
    Marker es_marker21 = new Marker();
    Marker es_marker22 = new Marker();
    Marker es_marker23 = new Marker();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        btnPrev = findViewById(R.id.btnPrev);
        myLocation = findViewById(R.id.facilities_mylocation);

        editSubwayName = findViewById(R.id.editSubwayName);
        btnsearch = findViewById(R.id.btnsearch);

        mapView = (MapView) findViewById(R.id.facilities_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
                overridePendingTransition(0,0);
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
                    if (editSubwayName.getText().toString().equals(afname)) {
                        resultname = xydataArr.get(i).getName();
                        resultx = xydataArr.get(i).getX();
                        resulty = xydataArr.get(i).getY();
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

    public void insert_nrmarker(){
        setMarker(nr_marker1, 37.570438861563,126.99211023609091,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker2, 37.57166399102276, 127.01073116025456,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker3, 37.56363407623313,126.97555753916346,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker4, 37.544621333410696, 127.05589389350534,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker5, 37.535253271078666, 127.09463831299745,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker6, 37.5132329245492,127.09993230518813,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker7, 37.50878951271656, 127.06281733633114,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker8, 37.498008893002215, 127.02781930790745,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker9, 37.48427349326003, 126.9298312990904,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker10, 37.493129336546836, 126.89498047850981,R.drawable.ic_facilities_nursing_room_marker,0);
        //10번까지가 대림역
        setMarker(nr_marker11, 37.5552066269044, 126.93679005634965,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker12, 37.525657028605664,126.89660919606361,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker13, 37.57475448632937, 127.02504889869374,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker14, 37.6361857552332, 126.91883779700947,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker15, 37.574272181312665, 126.95810116959169,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker16, 37.54149779646407, 127.01743393599295,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker17, 37.50487322322742, 127.00498757645461,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker18, 37.48461817329828, 127.03445699468733,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker19, 37.490912801175185, 127.0553110838705,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker20, 37.656322421788246, 127.06328139930872,R.drawable.ic_facilities_nursing_room_marker,0);
        //20번까지가 노원역
        setMarker(nr_marker21, 37.613421621635794, 127.03008499508303,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker22, 37.60326639757704, 127.0250301441778,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker23, 37.56499460051457, 127.00780146915163,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker24, 37.55283497462575, 126.97259602799788,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker25, 37.522264515275204, 126.97476221027905,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker26, 37.47737767273229, 126.98173022759005,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker27, 37.56194889311146, 126.80125324644031,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker28, 37.54854184600462, 126.8363353592136,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker29, 37.53208529583293, 126.84654808333123,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker30, 37.52608431509176, 126.86471756343083,R.drawable.ic_facilities_nursing_room_marker,0);
        //30번까지가 목동역
        setMarker(nr_marker31, 37.52427712040291, 126.89504995871766,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker32, 37.5177246307039, 126.91469047409603,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker33, 37.52155125945949,126.92403000387488,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker34, 37.52697605062431, 126.93270725409953,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker35, 37.54434296193473, 126.95131155816746,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker36, 37.560299348364964, 126.96291917465932,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker37, 37.57164631117466, 126.97619448887494,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker38, 37.564746989176214, 127.00473972346177,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker39, 37.56030889670109, 127.01356753780907,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker40, 37.561889603794825, 127.03709293178798,R.drawable.ic_facilities_nursing_room_marker,0);
        //40번까지가 왕십리역
        setMarker(nr_marker41, 37.56685265334438, 127.05272135537841,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker42, 37.557224195203, 127.07924869037716,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker43, 37.552137421757514, 127.0895982054048,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker44, 37.53841540368234, 127.12421625237545,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker45, 37.53592441167745, 127.13226230007736,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker46, 37.55500373151489, 127.15400251646867,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker47, 37.51617152927029, 127.13084744097019,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker48, 37.49313090306489, 127.14412230864488,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker49, 37.55745102547202, 127.17580247263854,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker50, 37.56296628739953, 127.19238848392936,R.drawable.ic_facilities_nursing_room_marker,0);
        //50번까지가 미사역
        setMarker(nr_marker51, 37.55245444305988, 127.20414797511248,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker52, 37.54207156237597, 127.20609983792285,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker53, 37.539774219361526, 127.22333183442673,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker54, 37.59840049640561, 126.91551851002505,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker55, 37.610811672339615, 126.92928220821192,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker56, 37.569317054245715, 126.89908725830205,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker57, 37.54885364584506, 126.91391661843822,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker58, 37.54776268134437, 126.9422648829556,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker59, 37.53565997101753, 126.97390346365916,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker60, 37.534545486274446, 126.99451828146142,R.drawable.ic_facilities_nursing_room_marker,0);
        //60번까지가 이태원역
        setMarker(nr_marker61, 37.554128386117995, 127.01028727700509,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker62, 37.60639454345622, 127.04847121402534,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker63, 37.57211617721146, 127.01575724194332,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker64, 37.586286339348014, 127.02884846390758,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker65, 37.677724699679246, 127.0553417836107,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker66, 37.654756181804565, 127.06048096984088,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker67, 37.63650720333626, 127.0679380574992,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker68, 37.6187037364307, 127.07531832998205,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker69, 37.595906148817456, 127.08566763292903,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker70, 37.49200137811063, 126.82358211659526,R.drawable.ic_facilities_nursing_room_marker,0);
        //70번까지가 온수역
        setMarker(nr_marker71, 37.54778703508827, 127.07457360523772,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker72, 37.531348487167314, 127.06671684731211,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker73, 37.511151366435264, 127.02155243275084,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker74, 37.503282951131794, 127.00492526865067,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker75, 37.48526147721817, 126.98157566697125,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker76, 37.49272753563565, 126.8965669685513,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker77, 37.48047903995303, 126.882844220569,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker78, 37.47932834125364, 126.85475100874898,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker79, 37.43399381424312, 127.12985682578447,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker80, 37.51762285487094, 127.11267786651548,R.drawable.ic_facilities_nursing_room_marker,0);
        //80번까지 몽촌토성역
        setMarker(nr_marker81, 37.514230451138864, 127.10294521679536 ,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker82, 37.49339869288822, 127.11795620616277,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker83, 37.47850098265749, 127.12623683316981,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker84, 37.451213970223236, 127.15967196520832,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker85, 37.46278857229522, 127.13920361039962,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker86, 37.50731019820045, 127.03386516107572,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker87, 37.504895040205525, 127.1066193576834,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker88, 37.51292773120434, 127.05289265238402,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker89, 37.51084515746574, 127.11257153715711,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker90, 37.51421270290199, 127.06013266637216,R.drawable.ic_facilities_nursing_room_marker,0);
        //90번까지 봉은사역
        setMarker(nr_marker91, 37.5164367593168, 127.11631836558041,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker92, 37.51135610615537, 127.07643470998123,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker93, 37.51567022621976, 127.12993600455843,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker94, 37.504631684748524, 127.08716637397974,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker95, 37.51935434737242, 127.13872040712096 ,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker96, 37.50243473176825, 127.09681067456012,R.drawable.ic_facilities_nursing_room_marker,0);
        setMarker(nr_marker97, 37.528780390388526, 127.14854648920972,R.drawable.ic_facilities_nursing_room_marker,0);
    }

    public void insert_elmarker(){
        setMarker(el_marker1,37.61249714582315, 127.10344861627428,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker2,37.61312780720865, 127.10348346808472,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker3,37.613456647714365, 127.10350657599761,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker4,37.616950180870305, 127.09272845076022 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker5,37.61740228932314, 127.09065055983976,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker6,37.62010109405623, 127.08409826252499,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker7,37.61723986465046, 127.07495156750365,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker8,37.61820599156977, 127.07523854000885,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker9,37.614999788605985, 127.06547765627205 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker10,37.61480077399582, 127.06689893532466 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker11,37.61101661402063, 127.07792399347807 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker12,37.6255728053379, 127.07326357224699 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker13,37.637286569643834, 127.06791327522212 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker14,37.63694890858137, 127.06755041799148,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker15,37.644949409431106, 127.06394027280929 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker16,37.60237980962135, 127.07915501010424 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker17,37.61103983912121, 127.05699685819786 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker18,37.62290568711701, 127.06180030330083 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker19,37.63196216717911, 127.05873483066138 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker20,37.64571928541454, 127.05102640633827 ,R.drawable.ic_facilities_elevator_marker,0);
        setMarker(el_marker21,37.64566078877242, 127.05087056495856 ,R.drawable.ic_facilities_elevator_marker,0);


    }

    public void insert_esmarker(){
        setMarker(es_marker1,37.6127093729313, 127.10288261009552,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker2,37.61646583352836, 127.09281279857902,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker3,37.61962317825069, 127.08464708629546 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker4,37.620914369634995, 127.08390944719812,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker5,37.617673815052385, 127.07616396625144,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker6,37.6179847104707, 127.07607933234117,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker7,37.61912718231457, 127.07534707458936,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker8,37.61444353833539, 127.06527895882613,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker9,37.614708496498544, 127.06676860010118,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker10,37.61548510513157, 127.06763576615533,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker11,37.61174680748798, 127.07732731627753 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker12,37.611163019009446,127.07793264056106 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker13,37.62587726115775, 127.07266065489577 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker14,37.63645722448718, 127.06867160421197 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker15,37.63607199746926, 127.06876755086293 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker16,37.63551393779125, 127.06781536138854 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker17,37.64406396986741, 127.06435025407195,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker18,37.644052417454304, 127.06487995728725,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker19,37.64594949936768, 127.06394396285573 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker20,37.601735516509436, 127.07928172631293 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker21,37.61092468405942, 127.05757155140759 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker22,37.610454364128714, 127.05664814720761 ,R.drawable.ic_facilities_escalator_marker,0);
        setMarker(es_marker23,37.63162652046349, 127.05879404310193 ,R.drawable.ic_facilities_escalator_marker,0);

    }

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
        insert_nrmarker();
        insert_elmarker();
        insert_esmarker();
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