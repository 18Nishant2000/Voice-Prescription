package com.example.e_prescription;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button mic,send;
    TextView out;
    EditText name,symptoms,diagnosis,prescription,advice;
    //String message,mobile;
    int c=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
      name=findViewById(R.id.name);
      symptoms=findViewById(R.id.symptoms);
      diagnosis=findViewById(R.id.diagnosis);
      prescription=findViewById(R.id.prescription);
      advice=findViewById(R.id.advice);
      mic=findViewById(R.id.mic_btn);

      //message=out.getText().toString();

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




    }

    public void createpdf(View view){
        PdfDocument pd=new PdfDocument();
        PdfDocument.PageInfo mPageInfo=new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page page=pd.startPage(mPageInfo);
        Paint mp=new Paint();
        page.getCanvas().drawText(name.getText().toString(),10,25,mp);

        pd.finishPage(page);
        String myFilePath= Environment.getExternalStorageDirectory().getPath()+"/pdffile.pdf";
        File myfile=new File(myFilePath);
        try {
            pd.writeTo(new FileOutputStream(myfile));
        }
        catch (Exception e){
            e.printStackTrace();
            name.setText("Error");
        }
        pd.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(c==0)
                    name.setText(result.get(0));
                    else if(c==1)
                        symptoms.setText(result.get(0));
                    else if(c==2)
                        diagnosis.setText(result.get(0));
                    else if(c==3)
                        prescription.setText(result.get(0));
                    else if(c==4)
                        advice.setText(result.get(0));
                    c++;
                }
        }
    }
}
