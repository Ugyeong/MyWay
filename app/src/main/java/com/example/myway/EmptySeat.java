package com.example.myway;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.view.textclassifier.TextLinks;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EmptySeat extends AppCompatActivity {

    ImageView btnPrev;
    ImageButton btnSearch;
    EditText editSubwayName;
    TextView upTextview1;
    TextView upTextview2;
    TextView dnTextview1;
    TextView dnTextview2;
    TextView TextdestUp;

    String apiURL = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/realtimeStationArrival/1/5/";
    String targetURL;

    Integer upNum = 0;
    Integer dnNum = 0;
    Integer chk = 0;

    URL url;
    InputStream is;
    XmlPullParserFactory parserCreator;
    XmlPullParser parser;
    OkHttpClient mClient;

    ArrayList<SubData> dataArr;
    //SubData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seat);

        btnPrev = findViewById(R.id.btnPrev);
        btnSearch = findViewById(R.id.btnsearch);
        editSubwayName = findViewById(R.id.editSubwayName);
        upTextview1 = findViewById(R.id.upTextview1);
        upTextview2 = findViewById(R.id.upTextview2);
        dnTextview1 = findViewById(R.id.dnTextview1);
        dnTextview2 = findViewById(R.id.dnTextview2);
        TextdestUp = findViewById(R.id.TextdestUp);

        targetURL = apiURL + editSubwayName.getText().toString();

        mClient = new OkHttpClient();


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //검색되어 방향이 바뀌도록 코드 추가

                API api = new API();
                //api.parse();  //dataArray 반환됨  ->가져다가 호선 나누고 상행, 하행 나눠서 도착시간 입력하기
                dataArr = api.parse();

                //api.t.start();
                Log.e("size", String.valueOf(dataArr.size()));
                for (SubData data : dataArr) {
                    Log.e("data",data.getDest());//출력해보기  -> 출력 안 됨
                    Log.e("data",data.getWay());//출력해보기
                    Log.e("data",data.getLine());//출력해보기
                    Log.e("data",data.getMinute());//출력해보기
                    //Log.e("data", data.getWay());
                    if (data.getWay().equals("상행")) {
                        TextdestUp.setText(data.getDest());
                        //Log.e("data", "저기");
                    }
                    //Log.e("data", "저기");
                }
            }
        });

    }

    public class SubData {
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

        public void setLine(String line) {
            this.line = line;
        }

        public String getLine() {
            return line;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public String getDest() {
            return dest;
        }

        public void setDest(String dest) {
            this.dest = dest;
        }
    }


    public class API {
        public ArrayList<SubData> parse() {
            ArrayList<SubData> dataArr = new ArrayList<SubData>();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e("touched", "touched");
                        url = new URL(targetURL);
                        is = url.openStream();
                        parserCreator = XmlPullParserFactory.newInstance();
                        parser = parserCreator.newPullParser();
                        parser.setInput(new InputStreamReader(is, "UTF-8"));
                        String tag = "";

                        //parser.next();

                        int parserEvent = parser.getEventType();

                        //Log.e("logcontinued","touched");
                        //SubData data = new SubData();
                        SubData data = new SubData();
                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            //Log.e("tag", "started");
                            //data = new SubData();
                            switch (parserEvent) {
                                /*case XmlPullParser.START_DOCUMENT:
                                    break;*/
                                case XmlPullParser.START_TAG:
                                    tag = parser.getName();
                                    if (tag.equals("row")) {
                                        data = new SubData();
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
                                           /*if (parser.getText().equals("상행")) {
                                               upNum += 1;
                                               chk = 1;  //상행일 경우
                                           }
                                           if (parser.getText().equals("하행")) {
                                               dnNum += 1;
                                               chk = 2;
                                           }*/
                                            break;
                                        }
                                        case "trainLineNm": {
                                            data.setDest(parser.getText());  //어디 행인지 입력받기
                                            //Log.e("data", data.getDest());//출력해보기
                                            break;
                                        }
                                        case "arvlMsg2": {
                                            //도착 시간
                                            data.setMinute(parser.getText());
                                            //Log.e("data", data.getMinute());//출력해보기
                                            //parser.next();
                                           /*if (upNum == 1 && chk == 1) {  //상행 중 가장 먼저 들어오는 열차인 경우
                                               upTextview1.setText(parser.getText());  //숫자 가져와서 입력
                                               Log.e("도착시간",parser.getText());
                                           }
                                           if (upNum == 2 && chk == 1) {  //상행 중 두 번째로 들어오는 열차인 경우
                                               upTextview2.setText(parser.getText());  //숫자 가져와서 입력
                                           }
                                           if (dnNum == 1 && chk == 2) {  //하행 중 가장 먼저 들어오는 열차인 경우
                                               dnTextview1.setText(parser.getText());  //숫자 가져와서 입력
                                           }
                                           if (dnNum == 2 && chk == 2) {  //하행 중 두 번째 들어오는 열차인 경우
                                               dnTextview2.setText(parser.getText());  //숫자 가져와서 입력
                                           }*/
                                            break;
                                        }
                                        //break;
                                    }

                                    /*if (tag.equals("updnLine")) {
                                        //parser.next();
                                        if (parser.getName().equals("상행")) {
                                            upNum += 1;
                                            chk = 1;  //상행일 경우
                                        }
                                        if (parser.getName().equals("하행")) {
                                            dnNum += 1;
                                            chk = 2;
                                        }
                                    } else if (tag.equals("arvlMsg2")) {
                                        parser.next();
                                        if (upNum == 1 && chk == 1) {  //상행 중 가장 먼저 들어오는 열차인 경우
                                            upTextview1.setText(parser.getText());  //숫자 가져와서 입력
                                        }
                                        if (upNum == 2 && chk == 1) {  //상행 중 두 번째로 들어오는 열차인 경우
                                            upTextview2.setText(parser.getText());  //숫자 가져와서 입력
                                        }
                                        if (dnNum == 1 && chk == 2) {  //하행 중 가장 먼저 들어오는 열차인 경우
                                            dnTextview1.setText(parser.getText());  //숫자 가져와서 입력
                                        }
                                        if (dnNum == 2 && chk == 2) {  //하행 중 두 번째 들어오는 열차인 경우
                                            dnTextview2.setText(parser.getText());  //숫자 가져와서 입력
                                        }
                                    }*/
                                //break;
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
}





