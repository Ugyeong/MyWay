package com.example.myway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class EmptySeat extends AppCompatActivity {

    ImageView btnPrev;
    ImageButton btnSearch;
    EditText editSubwayName;
    TextView upTextview1;
    TextView upTextview2;
    TextView dnTextview1;
    TextView dnTextview2;

    String apiURL = "http://swopenapi.seoul.go.kr/api/subway/sample/xml/realtimeStationArrival/1/5/";
    String targetURL;

    Integer upNum=0;
    Integer dnNum=0;
    Integer chk=0;

    URL url;
    InputStream is;
    XmlPullParserFactory parserCreator;
    XmlPullParser parser;


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
        dnTextview2 =findViewById(R.id.dnTextview2);

        API();

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
            }
        });
    }

    public void API(){
        targetURL=apiURL+editSubwayName.getText().toString();
        try {
            url = new URL(targetURL);
            is = url.openStream();
            parserCreator = XmlPullParserFactory.newInstance();
            parser = parserCreator.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            String tag;

            parser.next();

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        tag=parser.getName();
                        if (tag.equals("row"));
                        else if(tag.equals("updnLine")){
                            //parser.next();
                            if(parser.getName().equals("상행")){
                                upNum+=1;
                                chk=1;  //상행일 경우
                            }
                            if(parser.getName().equals("하행")){
                                dnNum+=1;
                                chk=2;
                            }
                        }
                        else if(tag.equals("arvlMsg2")){
                            parser.next();
                            if(upNum==1 && chk==1){  //상행 중 가장 먼저 들어오는 열차인 경우
                                upTextview1.setText(parser.getText());  //숫자 가져와서 입력
                            }
                            if(upNum==2 && chk==1){  //상행 중 두 번째로 들어오는 열차인 경우
                                upTextview2.setText(parser.getText());  //숫자 가져와서 입력
                            }
                            if(dnNum==1 && chk==2){  //하행 중 가장 먼저 들어오는 열차인 경우
                                dnTextview1.setText(parser.getText());  //숫자 가져와서 입력
                            }
                            if(dnNum==2 && chk==2){  //하행 중 두 번째 들어오는 열차인 경우
                                dnTextview2.setText(parser.getText());  //숫자 가져와서 입력
                            }
                        }

                }
                parserEvent=parser.next();
            }
        } catch (MalformedURLException | XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}