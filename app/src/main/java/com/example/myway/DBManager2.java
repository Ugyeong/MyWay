package com.example.myway;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager2 extends SQLiteOpenHelper {

    public DBManager2(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists hospital (name text, code text primary key)");
        sqLiteDatabase.execSQL("insert into hospital values('BlueWhale','20221111');"); // 회원가입에서 임산부 인증에 대한 기능을 테스트하기 위해 임의로 넣어놓는 데이터이다.
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
