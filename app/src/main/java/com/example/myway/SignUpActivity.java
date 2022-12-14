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
    EditText EditHospitalCode;
    EditText EditPhoneNumber;
    EditText EditNumberChk;


    Button BtnEmailChk;
    Button  BtnSignUp;
    Button  BtnPhoneNumber;
    Button BtnHospitalCode;

    TextView TvEmailWarning;
    TextView TvpasswordChkWarning;
    TextView TvHospitalCodeWarning;

    String name;
    String email;
    String password;
    String passwordchk;
    String hospital;
    String hospitalCode;
    String phonenumber;
    String numberchk;

    private String compare_hash;


    //DB??? ????????? ??? ????????? ????????????
    DBManager DBManager;
    DBManager2 DBManager2;
    SQLiteDatabase db;
    Cursor cursor;

    SmsReceiver SmsReceiver; // SMS ??????
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
        EditHospitalCode = findViewById(R.id.EditHospitalCode);
        EditPhoneNumber = findViewById(R.id.EditPhoneNumber);
        EditNumberChk = findViewById(R.id.EditNumberChk);

        BtnEmailChk = findViewById(R.id.BtnEmailChk);
        BtnSignUp = findViewById(R.id.BtnSignUp);
        BtnPhoneNumber = findViewById(R.id.BtnPhoneNumber);
        BtnHospitalCode = findViewById(R.id.BtnHospitalCode);

        TvEmailWarning = findViewById(R.id.TvEmailWarning);
        TvpasswordChkWarning = findViewById(R.id.TvPasswordChkWarning);
        TvHospitalCodeWarning = findViewById(R.id.TvHospitalCodeWarning);


       SmsReceiver = new SmsReceiver();
       ObservableObject.getInstance().addObserver(this);

        //SMS ????????? ???????????? ????????? ???????????? ????????? ????????? ????????????
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

       else { // ????????? ?????? ???????????? ?????? ???
       }

       BtnEmailChk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               DBManager = new DBManager(getApplicationContext(), "MyWay", null,1); //MyWay?????? DB??? ??????
               db = DBManager.getReadableDatabase(); // select??? ????????? ???????????? DB??? ?????? ??? ????????? ???

               email = EditEmail.getText().toString();
               if(email.isEmpty()){
                   TvEmailWarning.setText("????????? ????????? ?????? ????????????");
                   return;
               }
               cursor = db.rawQuery("select * from account where email='"+ email+"';", null);

               AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this); // dialog?????? ????????? ?????? ??????

               if(cursor.getCount()>0){ // ?????? ????????? ???????????? ???
                   dlg.setTitle("??????????????? ?????????");
                   dlg.setMessage("?????? ???????????? ??????????????????\n???????????? ??????????????? ????????? ??? ????????????");
                   dlg.setIcon(R.drawable.app_icon_my);
                   dlg.setPositiveButton("??????", null);
                   dlg.show();
                   EditEmail.setText("");
               }
               else{
                   dlg.setTitle("??????????????? ?????????");
                   dlg.setMessage("??????????????? ??????????????????");
                   dlg.setIcon(R.drawable.app_icon_my);
                   dlg.setPositiveButton("??????", null);
                   dlg.show();

                   EditPassword.setEnabled(true);
                   EditPasswordChk.setEnabled(true);
                   BtnPhoneNumber.setEnabled(true);

               }
               cursor.close();
               db.close();
               DBManager.close();

           }
       });

       BtnPhoneNumber.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               phonenumber = EditPhoneNumber.getText().toString();

               if(!(phonenumber.length() ==11)){ //???????????? ????????? ?????? ????????? ????????? ???
                   AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this); // dialog?????? ????????? ?????? ??????
                   dlg.setTitle("???????????? ?????? ??????");
                   dlg.setMessage("????????? ????????? ?????? ???????????????\n ?????? ??????????????? ????????? ??? ???????????? ");
                   dlg.setIcon(R.drawable.app_icon_my);
                   dlg.setPositiveButton("??????", null);
                   dlg.show();
                   EditPhoneNumber.setText("");
               }
               else{ // ????????? ????????? ??????????????? ???????????? ???
                   checkPermission();
                   SmsManager sms = SmsManager.getDefault();
                   String phoneNumber = EditPhoneNumber.getText().toString();
                   String key_hash = getAppSignatures(getApplicationContext());
                   String message ="<#> MyWay ?????? ??????????????? ????????? ????????????.\n"+key_hash;
                   sms.sendTextMessage(phoneNumber, null, message, null, null);
                   compare_hash = key_hash;

                   // ????????? ????????? ??????????????? ???????????? ??? ????????? ????????????, ????????? ????????? ??????????????? ??????
                   EditNumberChk.setEnabled(true);
                   EditNumberChk.setText(compare_hash);
                   EditHospital.setEnabled(true);
                   EditHospitalCode.setEnabled(true);
                   BtnHospitalCode.setEnabled(true);



               }

           }

       });
       BtnHospitalCode.setOnClickListener(new View.OnClickListener() { // ????????? ?????? ?????? ????????? ???
           @Override
           public void onClick(View view) {
               hospital = EditHospital.getText().toString();
               hospitalCode = EditHospitalCode.getText().toString();

               if(hospital.isEmpty() || hospitalCode.isEmpty()){ // ??? ???????????? ???????????? ???????????? ????????? ???
                   AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this); // dialog?????? ????????? ?????? ??????
                   dlg.setTitle("????????? ?????? ??????");
                   dlg.setMessage("???????????? ?????? ??????????????? ?????? ???????????????.");
                   dlg.setIcon(R.drawable.app_icon_my);
                   dlg.setPositiveButton("??????", null);
                   dlg.show();
                   return;
               }
               else{
                   DBManager2 = new DBManager2(getApplicationContext(), "MyWayHospital", null,1); //MyWayHospital?????? DB??? ????????????
                   db = DBManager2.getReadableDatabase(); // select??? ????????? ???????????? DB ???????????? ?????? ??? ????????? ???
                   cursor = db.rawQuery("select * from hospital where name='"+ hospital+"'and code='"+hospitalCode+"';", null);

                   if(cursor.getCount()>0){ //????????? ?????? ??????
                       TvHospitalCodeWarning.setTextColor(Color.parseColor("#407BFF"));
                       TvHospitalCodeWarning.setText("????????? ?????? ??????");
                       BtnSignUp.setEnabled(true);

                   }
                   else{ //????????? ?????? ??????
                       TvHospitalCodeWarning.setTextColor(Color.parseColor("#FF0000")); //?????????
                       TvHospitalCodeWarning.setText("????????? ?????? ??????\n???????????? ????????? ?????? ??? ????????????");
                   }

                   cursor.close();
                   db.close();
                   DBManager2.close();
               }

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
                hospitalCode = EditHospitalCode.getText().toString();
                phonenumber = EditPhoneNumber.getText().toString();
                numberchk = EditNumberChk.getText().toString();


                DBManager = new DBManager(getApplicationContext(), "MyWay", null,1); //MyWay?????? DB??? ????????????
                db = DBManager.getWritableDatabase(); // insert??? ????????? ???????????? DB ???????????? ??? ??? ????????? ???

                AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this); // dialog?????? ????????? ?????? ??????

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordchk.isEmpty() || hospital.isEmpty() || hospitalCode.isEmpty() ||phonenumber.isEmpty() || numberchk.isEmpty()){
                    dlg.setTitle("???????????? ??????");
                    dlg.setMessage("??????????????? ?????? ???????????????.");
                    dlg.setIcon(R.drawable.app_icon_my);
                    dlg.setPositiveButton("??????", null);
                    dlg.show();
                    return;
                }
                else{
                    if(EditNumberChk.getText().toString().equals(compare_hash)){ //???????????? ???????????? ??? DB??? ????????? ??????
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
                        dlg.setTitle("???????????? ??????");
                        dlg.setMessage("??????????????? ?????? ?????????????????? ????????????.");
                        dlg.setIcon(R.drawable.app_icon_my);
                        dlg.setPositiveButton("??????", null);
                        dlg.show();
                    }

                }

            }
        });


        EditEmail.addTextChangedListener(new TextWatcher() { // ???????????? ????????? ??? ????????? ????????? ????????????
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editable.toString()).matches()){ //????????? ????????? ?????? ?????? ???
                    TvEmailWarning.setText("????????? ????????? ?????? ????????????\n??????????????? ?????? ??? ????????????"); //textView??? ????????????
                }
                else{ //????????? ????????? ?????? ???
                    BtnEmailChk.setEnabled(true);
                    TvEmailWarning.setText("");
                }

            }
        });

        EditPasswordChk.addTextChangedListener(new TextWatcher() { // ??????????????? ?????? ????????? ??? ??????????????? ????????????
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!EditPassword.getText().toString().equals(EditPasswordChk.getText().toString())){ //??????????????? ???????????? ?????? ???

                    TvpasswordChkWarning.setText("??????????????? ???????????? ????????????");
                }
                else{//??????????????? ????????? ???
                    TvpasswordChkWarning.setText("");
                    EditPhoneNumber.setEnabled(true); // ??????????????? ????????? ??? ????????? ????????? ????????? ??? ????????? ??????
                    EditNumberChk.setEnabled(true); // ????????? ????????? ??????????????? ???
                }
            }
        });
    }

    public void checkPermission(){
        String[] permission_list = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS
        };

        //?????? ??????????????? ????????? 6.0???????????? ???????????? ????????????.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //?????? ?????? ????????? ????????????.
            int chk = checkCallingOrSelfPermission(permission);
            if(chk == PackageManager.PERMISSION_DENIED){
                //?????? ?????????????????? ???????????? ?????? ?????????
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
            }
        } catch (PackageManager.NameNotFoundException e) {

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


            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
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

    }
    public void set_module(){

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // retriever ??????
                if(SmsReceiver != null){
                    unregisterReceiver(SmsReceiver);
                }

                SmsReceiver = new SmsReceiver();
                IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);    // SMS_RETRIEVED_ACTION ??????
                registerReceiver(SmsReceiver, intentFilter);
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // retriever ??????
            }
        });
    }
}