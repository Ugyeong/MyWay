package com.example.myway;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class EmptySeat extends AppCompatActivity {

    ImageView btnPrev;
    ImageButton btnSearch;
    EditText editSubwayName;
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

    String apiURL = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/realtimeStationArrival/1/5/";
    String targetURL;

    Integer upNum = 0;
    Integer dnNum = 0;

    URL url;
    InputStream is;
    XmlPullParserFactory parserCreator;
    XmlPullParser parser;

    ArrayList<SubData> dataArr;

    String subwayName;
    String targetSubName;
    String targetSubLine;

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
        TextdestDn = findViewById(R.id.TextdestDn);
        destUp1 = findViewById(R.id.destUp1);
        destUp2 = findViewById(R.id.destUp2);
        destDn1 = findViewById(R.id.destDn1);
        destDn2 = findViewById(R.id.destDn2);


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????? ??????????????? ??????
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????? ??? ??? ???????????? ????????????
                try {
                    subwayName = editSubwayName.getText().toString();
                    targetSubName = subwayName.substring(0, subwayName.indexOf("???"));
                    targetSubLine = subwayName.substring(subwayName.indexOf("???")+2,subwayName.indexOf("???")+3);
                    targetURL = apiURL + targetSubName;
                } catch (Exception e) {
                    Toast.makeText(EmptySeat.this, "",Toast.LENGTH_SHORT);
                }

                //API????????????
                API api = new API();
                dataArr = api.parse();

                for (SubData data : dataArr) {

                     //?????? ?????? ?????? ??????
                    if(data.getLine().equals("100"+targetSubLine)) {

                        if (data.getWay().equals("??????") && upNum == 0) {
                            destUp1.setText(data.getDest().substring(0, data.getDest().indexOf("???")));  //?????? ?????? ????????????
                            destUp2.setText(data.getDest().substring(0, data.getDest().indexOf("???")));  //??????????????? ?????? ??? ???????????? ?????? ????????? ?????? ???????????? ?????? ?????????
                            TextdestUp.setText(data.getDest().substring(data.getDest().indexOf("-") + 2));
                            if (data.getMinute().indexOf("???") != -1) {
                                upTextview1.setText(data.getMinute().substring(0, data.getMinute().indexOf("???")));
                            } else {
                                upTextview1.setText(data.getMinute());
                            }
                            upNum += 1;
                            continue;
                        }
                        if (data.getWay().equals("??????") && dnNum == 0) {
                            destDn1.setText(data.getDest().substring(0, data.getDest().indexOf("???")));
                            destDn2.setText(data.getDest().substring(0, data.getDest().indexOf("???")));
                            TextdestDn.setText(data.getDest().substring(data.getDest().indexOf("-") + 2));
                            if (data.getMinute().indexOf("???") != -1) {
                                dnTextview1.setText(data.getMinute().substring(0, data.getMinute().indexOf("???")));  //???????????? ???????????? ??????
                            } else {
                                dnTextview1.setText(data.getMinute());
                            }
                            dnNum += 1;
                            continue;
                        }
                        if (data.getWay().equals("??????") && upNum == 1) {
                            destUp2.setText(data.getDest().substring(0, data.getDest().indexOf("???")));  //?????? ?????? ????????????
                            if (data.getMinute().indexOf("???") != -1) {
                                upTextview2.setText(data.getMinute().substring(0, data.getMinute().indexOf("???")));
                            } else {
                                upTextview2.setText(data.getMinute());
                            }
                            upNum += 1;  //??? ????????? ???????????? ?????? ??????
                            continue;
                        }
                        if (data.getWay().equals("??????") && dnNum == 1) {
                            destDn2.setText(data.getDest().substring(0, data.getDest().indexOf("???")));
                            if (data.getMinute().indexOf("???") != -1) {
                                dnTextview2.setText(data.getMinute().substring(0, data.getMinute().indexOf("???")));
                            } else {
                                dnTextview2.setText(data.getMinute());
                            }
                            dnNum += 1;  //??? ????????? ???????????? ?????? ??????
                            continue;
                        }
                    }
                    else{
                    }

                }
            }
        });
        btnSearch.performClick();  //??????????????? ???????????? ?????? ??????????????? ??????

    }

    public class SubData {
        String way;  //??????, ??????
        String minute;  //??? ??? ??? ??????
        String line;   //??? ??????
        String dest;  //??????

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

                        int parserEvent = parser.getEventType();

                        SubData data = new SubData();
                        while (parserEvent != XmlPullParser.END_DOCUMENT) {
                            switch (parserEvent) {
                                case XmlPullParser.START_TAG:
                                    tag = parser.getName();
                                    if (tag.equals("row")) {
                                        data = new SubData();
                                    }
                                case XmlPullParser.END_TAG:
                                    if (tag.equals("row")) {
                                        dataArr.add(data);

                                        Log.e("please", String.valueOf(dataArr.size()));

                                    }
                                    break;
                                case XmlPullParser.TEXT:
                                    switch (tag) {
                                        case "subwayId": {
                                            data.setLine(parser.getText());  //?????? ????????????
                                            break;
                                        }
                                        case "updnLine": {
                                            data.setWay(parser.getText());  //?????? ????????????
                                            break;
                                        }
                                        case "trainLineNm": {
                                            data.setDest(parser.getText());  //?????? ????????? ????????????

                                            break;
                                        }
                                        case "arvlMsg2": {
                                            data.setMinute(parser.getText());  //?????? ??????
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
            return dataArr;
        }
    }
}
