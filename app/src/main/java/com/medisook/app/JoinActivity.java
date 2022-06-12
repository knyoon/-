package com.medisook.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_nickname;
    private EditText et_password;
    private EditText warn_nk;
    private EditText warn_pw;
    private Button join_btn;
    private Context mContext;
    boolean nk_result=false;
    boolean pw_result = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        warn_nk = (EditText) findViewById(R.id.warn_nk);
        warn_pw = (EditText) findViewById(R.id.warn_pw);
        et_nickname = (EditText) findViewById(R.id.nickname);
        join_btn = (Button) findViewById(R.id.join_btn);
        join_btn.setOnClickListener(this);

        et_nickname.setOnClickListener(this);
        et_nickname.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String nk =  et_nickname.getText().toString();
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == keyEvent.ACTION_UP) {
                            if(nk.length() >= 11 || nk.length()==0) {
                                et_nickname.setText(null);
                                warn_nk.setText("!잘못된 형식입니다.");
                                nk_result= false;
                            }
                            else{
                                warn_nk.setText(null);
                                nk_result= true;
                            }
                            Log.d("회원가입", "result: " + nk_result);
                            Log.d("회원가입", "닉네임: " + nk);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            //getWindow().sethideSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        return true;
                    case KeyEvent.KEYCODE_DEL:
                }
                return false;
            }
        });

        et_password = (EditText) findViewById(R.id.password);
        et_password.setOnClickListener(this);
        et_password.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String pw = et_password.getText().toString();
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == keyEvent.ACTION_UP) {
                            if(Pattern.matches("^[A-Za-z0-9]{8,12}$", pw)) {
                                warn_pw.setText(null);
                                pw_result = true;
                            }
                            else {
                                et_password.setText(null);
                                warn_pw.setText("!잘못된 형식입니다.");
                                pw_result = false;
                            }
                            Log.d("회원가입", "패스워드: " + pw_result);
                            Log.d("회원가입","패스워드: " + pw);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        } return true;
                    case KeyEvent.KEYCODE_DEL:
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        //Log.v("회원가입", "테스트: " + nk_result);
        //Log.v("회원가입", "테스트: " + pw_result);
        switch (v.getId()){
            case R.id.join_btn:
                Log.v("회원가입", "테스트- " + nk_result + pw_result);
                if(nk_result == true | pw_result == true){
                    Toast.makeText(this.getApplicationContext(),"회원가입 성공!", Toast.LENGTH_SHORT).show();
                    Intent intentMainActivity =
                            new Intent(JoinActivity.this, LoginActivity.class);
                    startActivity(intentMainActivity);
                }
                else{
                    Toast.makeText(this.getApplicationContext(),"회원가입을 실패했습니다.\n다시 시도하십시오.", Toast.LENGTH_SHORT).show();
                    et_password.setText(null);
                    et_nickname.setText(null);
                }
        }
    }
}
