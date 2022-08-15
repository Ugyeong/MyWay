package com.example.myway;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, Observer {

    EditText EditName;
    EditText EditEmail;
    EditText EditPassword;
    EditText EditPasswordChk;
    EditText EditHospital;
    EditText EditPhoneNumber;
    EditText EditNumberChk;

    Button BtnEmailChk;
    Button  BtnSignUp;
    Button  BtnPhoneNumber;

    TextView TvEmailWarning;
    TextView TvpasswordChkWarning;

    String name;
    String email;
    String password;
    String passwordchk;
    String hospital;
    String phonenumber;
    String numberchk;
    private String compare_hash;


    //DB에 접근할 때 사용할 변수선언
    DBManager DBManager;
    SQLiteDatabase db;
    Cursor cursor;

    SmsReceiver SmsReceiver; // SMS 인증
    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_HASHED_BYTES = 9;
    public static final int NUM_BASE64_CHAR = 11;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditName = findViewById(R.id.EditName);
        EditEmail = findViewById(R.id.EditEmail);
        EditPassword = findViewById(R.id.EditPassword);
        EditPasswordChk  = findViewById(R.id.EditPasswordChk);
        EditHospital = findViewById(R.id.EditHospital);
        EditPhoneNumber = findViewById(R.id.EditPhoneNumber);
        EditNumberChk = findViewById(R.id.EditNumberChk);
        BtnEmailChk = findViewById(R.id.BtnEmailChk);
        BtnSignUp = findViewById(R.id.BtnSignUp);
        BtnPhoneNumber = findViewById(R.id.BtnPhoneNumber);
        TvEmailWarning = findViewById(R.id.TvEmailWarning);
        TvpasswordChkWarning = findViewById(R.id.TvPasswordChkWarning);


       SmsReceiver = new SmsReceiver();
       ObservableObject.getInstance().addObserver(this);

        //SMS 권한이 부여되어 있는지 확인하고 없으면 권한을 부여한다
       if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) +
               ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS))
               != PackageManager.PERMISSION_GRANTED) {


           if (ActivityCompat.shouldShowRequestPermissionRationale(this,"Manifest.permission.READ_SMS") ||
                   ActivityCompat.shouldShowRequestPermissionRationale(this,"Manifest.permission.READ_SMS")) {

           } else {
               ActivityCompat.requestPermissions(this,
                       new String[]{"Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS"},
                       REQUEST_CODE);

           }
       }

       else { // 권한이 이미 부여되어 있을 때
       }

       BtnEmailChk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               DBManager = new DBManager(getApplicationContext(), "MyWay", null,1); //MyWay라는 DB를 생성
               db = DBManager.getReadableDatabase(); // select문 실행할 것이므로 DB를 읽을 수 있도록 함

               email = EditEmail.getText().toString();
               cursor = db.rawQuery("select * from account where email='"+ email+"';", null);

               AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this); // dialog창을 띄우기 위해 선언

               if(cursor.getCount()>0){ // 이미 가입한 이메일일 때
                   dlg.setTitle("사용불가한 이메일");
                   dlg.setMessage("이미 존재하는 이메일입니다");
                   dlg.setIcon(R.drawable.app_icon_my);
                   dlg.setPositiveButton("확인", null);
                   dlg.show();
                   EditEmail.setText("");
               }
               else{
                   dlg.setTitle("사용가능한 이메일");
                   dlg.setMessage("사용가능한 이메일입니다");
                   dlg.setIcon(R.drawable.app_icon_my);
                   dlg.setPositiveButton("확인", null);
                   dlg.show();
                   EditPassword.requestFocus();
               }
               cursor.close();
               db.close();
               DBManager.close();

           }
       });

       BtnPhoneNumber.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               checkPermission();

               SmsManager sms = SmsManager.getDefault();
               String phoneNumber = EditPhoneNumber.getText().toString();
               String key_hash = getAppSignatures(getApplicationContext());
               String message ="<#> MyWay 앱의 인증번호는 다음과 같습니다.\n"+key_hash;
               sms.sendTextMessage(phoneNumber, null, message, null, null);
               compare_hash = key_hash;
               EditNumberChk.setText(compare_hash);
           }

       });


        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = EditName.getText().toString();
                email = EditEmail.getText().toString();
                password = EditPassword.getText().toString();
                passwordchk = EditPasswordChk.getText().toString();
                hospital = EditHospital.getText().toString();
                phonenumber = EditPhoneNumber.getText().toString();
                numberchk = EditNumberChk.getText().toString();

                DBManager = new DBManager(getApplicationContext(), "MyWay", null,1); //MyWay라는 DB에 접근가능
                db = DBManager.getWritableDatabase(); // insert문 실행할 것이므로 DB에 데이터를 쓸 수 있도록 함

                AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this); // dialog창을 띄우기 위해 선언

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordchk.isEmpty() || hospital.isEmpty() || phonenumber.isEmpty() || numberchk.isEmpty()){
                    dlg.setTitle("회원가입 실패");
                    dlg.setMessage("회원정보를 전부 입력하세요.");
                    dlg.setIcon(R.drawable.app_icon_my);
                    dlg.setPositiveButton("확인", null);
                    dlg.show();
                    return;
                }
                else{
                    if(EditNumberChk.getText().toString().equals(compare_hash)){ //본인인증 성공했을 때 DB에 데이터 저장
                        try{
                            synchronized (db){
                                db.execSQL("insert into account values('"+name+"','"+email+"','"+password+"','"+hospital+"');");
                            }
                        }
                        finally {
                            if(db != null && db.isOpen()){
                                db.close();
                                DBManager.close();
                            }
                        }
                        Intent intent = new Intent(getApplicationContext(), SignUpSuccessActivity.class);
                        startActivity(intent);
                    }
                    else{
                        dlg.setTitle("본인인증 실패");
                        dlg.setMessage("본인인증을 다시 시도해주시길 바랍니다.");
                        dlg.setIcon(R.drawable.app_icon_my);
                        dlg.setPositiveButton("확인", null);
                        dlg.show();
                    }

                }

            }
        });


        EditEmail.addTextChangedListener(new TextWatcher() { // 이메일을 입력할 때 이메일 형식을 확인한다
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()){ //이메일 형식에 맞지 않을 때
                    TvEmailWarning.setText("이메일 형식에 맞지 않습니다"); //textView에 출력하기
                }
                else{

                    TvEmailWarning.setText("");
                }

            }
        });

        EditPasswordChk.addTextChangedListener(new TextWatcher() { // 비밀번호를 다시 입력할 때 일치하는지 확인한다
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!EditPassword.getText().toString().equals(EditPasswordChk.getText().toString())){ //비밀번호가 일치하지 않을 때

                    TvpasswordChkWarning.setText("비밀번호가 일치하지 않습니다");
                }
                else{//비밀번호가 일치할 때
                    TvpasswordChkWarning.setText("");
                }

            }
        });
    }

    public void checkPermission(){
        String[] permission_list = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
        };

        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }
    public static String getAppSignatures(Context context) {
        ArrayList<String> appCodes = new ArrayList<>();
        String hash="";
        try {
            // Get all package signatures for the current package
            String packageName = context.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;

            // For each signature create a compatible hash
            for (Signature signature : signatures) {
                hash = getHash(packageName, signature.toCharsString());
                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
                Log.d("TAG", String.format("이 값을 SMS 뒤에 써서 보내주면 됩니다 : %s", hash));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("TAG", "Unable to find package to obtain hash. : " + e.toString());
        }
        return hash;
    }
    private static String getHash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
            byte[] hashSignature = messageDigest.digest();

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
            // encode into Base64
            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);

            Log.d("TAG", String.format("\nPackage : %s\nHash : %s", packageName, base64Hash));
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            Log.d("TAG", "hash:NoSuchAlgorithm : " + e.toString());
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
                registerReceiver(SmsReceiver, intentFilter);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void update(Observable observable, Object data) {
        String message = (String)data;
        set_module();
        Log.d("TAG","update : massage - "+message);

    }
    public void set_module(){

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // retriever 성공
                if(SmsReceiver != null){
                    unregisterReceiver(SmsReceiver);
                }

                SmsReceiver = new SmsReceiver();
                IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);    // SMS_RETRIEVED_ACTION 필수
                registerReceiver(SmsReceiver, intentFilter);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // retriever 실패
            }
        });
    }
}