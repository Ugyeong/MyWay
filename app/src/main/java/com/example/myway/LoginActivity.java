package com.example.myway;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText EditEmail;
    EditText EditPassword;
    Button BtnLogin;
    TextView TvSingUp;
    String email;
    String password;
    String hospital;
    String name;

    //자동로그인을 위한 문자열
    String loginId;
    String loginPwd;

    DBManager DBManager;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditEmail = findViewById(R.id.EditEmail);
        EditPassword = findViewById(R.id.EditPassword);
        BtnLogin = findViewById(R.id.BtnLogin);
        TvSingUp = findViewById(R.id.TvSingUp);


        //로그인 유지 기능을 위한 코드
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
        loginId = sharedPreferences.getString("inputId", null);
        loginPwd = sharedPreferences.getString("inputPwd", null);

        if(loginId != null && loginPwd != null){ // 자동로그인 성공하면 바로 MainActivity로 이동하기
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = EditEmail.getText().toString();
                password = EditPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty() ){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
                    dlg.setTitle("로그인 실패");
                    dlg.setMessage("이메일과 비밀번호를 모두 입력하세요.");
                    dlg.setIcon(R.drawable.app_icon_my);
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                }
                else{
                   if(login(email, password)){

                       SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);

                       SharedPreferences.Editor autoLogin = sharedPreferences.edit();

                       autoLogin.putString("inputId", email);
                       autoLogin.putString("inputPwd", password);
                       autoLogin.putString("name", name);
                       autoLogin.putString("hospital", hospital);

                       autoLogin.commit();

                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(intent);
                   }
                }

            }
        });


        TvSingUp.setOnClickListener(new View.OnClickListener() { //회원가입 액티비티로 이동하기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
    @SuppressLint("Range")
    private Boolean login (String email, String password){

        DBManager = new DBManager(getApplicationContext(), "MyWay", null,1); //MyWay라는 DB를 생성
        db = DBManager.getReadableDatabase(); // select문 실행할 것이므로 DB를 읽을 수 있도록 함
        cursor = db.rawQuery("select * from account where email='"+ email+"';", null);

        if(cursor.getCount()>0){ //입력한 이메일을 사용하는 회원이 있을 때
            cursor.moveToNext();
            if(password.equals(cursor.getString(2))){ //이메일과 비밀번호가 일치할 때
                hospital = cursor.getString(cursor.getColumnIndex("hospital")); //해당 회원의 병원이름을 저장해둔다
                name = cursor.getString(cursor.getColumnIndex("name"));
                cursor.close();
                db.close();
                DBManager.close();
                return true;
            }
            else{ // 입력한 이메일을 사용하는 회원이 있지만 해당 비밀번호가 일치하지 않을 때
                AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
                dlg.setTitle("로그인 실패");
                dlg.setMessage("아이디 또는 비밀번호가 일치하지 않습니다.");
                dlg.setIcon(R.drawable.app_icon_my);
                dlg.setPositiveButton("확인", null);
                dlg.show();
            }
        }
        else{ //입력한 이메일을 사용하는 회원이 없을 때
            AlertDialog.Builder dlg = new AlertDialog.Builder(LoginActivity.this);
            dlg.setTitle("로그인 실패");
            dlg.setMessage("회원이 아닙니다.");
            dlg.setIcon(R.drawable.app_icon_my);
            dlg.setPositiveButton("확인", null);
            dlg.show();
        }

        //로그인에 실패했을 때 다시 입력하도록 텍스트들을 지운다.
        EditEmail.setText("");
        EditPassword.setText("");
        EditEmail.requestFocus();

        cursor.close();
        db.close();
        DBManager.close();
        return false;
    }
}