package com.example.e_prescription;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button mic,send;
    TextView out;
    EditText pno;
    String message,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      mic=findViewById(R.id.mic_btn);
      out=findViewById(R.id.out);
      pno=findViewById(R.id.pno);
      send=findViewById(R.id.send);

      message=out.getText().toString();
      mobile=pno.getText().toString().trim();

      mic.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
              intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
              intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
              intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak Something");
              try {
                  startActivityForResult(intent,1000);
              }catch (Exception e){
                  Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
              }
          }
      });

      send.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              try {
                  SmsManager smsManager=SmsManager.getDefault();
                  smsManager.sendTextMessage(mobile,null,message,null,null);
                  Toast.makeText(MainActivity.this, "MESSAGE SENT", Toast.LENGTH_SHORT).show();
              }
              catch (Exception e){
                  Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
              }

          }
      });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    out.setText(result.get(0));
                }
        }
    }
}
