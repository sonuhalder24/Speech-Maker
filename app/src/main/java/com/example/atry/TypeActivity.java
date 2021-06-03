package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class TypeActivity extends AppCompatActivity {
    EditText editText;
    Button buttonStart,buttonStop;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        editText=findViewById(R.id.editText);
        buttonStart=findViewById(R.id.btnStart);
        buttonStop=findViewById(R.id.btnStop);

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int lang=textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if(editText.getText().toString().trim().length()<=textToSpeech.getMaxSpeechInputLength()) {
                        int speech = textToSpeech.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else{
                        Toast.makeText(TypeActivity.this, "Its too much long", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                int speech=textToSpeech.speak(editText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }
    @Override
    protected void onDestroy() {

        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onDestroy();
    }
}