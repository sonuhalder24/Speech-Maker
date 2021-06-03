package com.example.atry;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class ViewActivity extends AppCompatActivity {
    Button buttonNext;
    ImageView btnPlay;
    String str,etn2Text;
    TextView textView,counting;
    int i=2;
    int j;
    int k;
    Uri data;
    int page;
    int count0,count1,count2;
    InputStream inputStream;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        page=getIntent().getIntExtra("pages",1);
        str=getIntent().getStringExtra("str");
        data=getIntent().getParcelableExtra("dataPDF");
        if(getIntent().getStringExtra("etn2")!=null) {
            etn2Text = getIntent().getStringExtra("etn2");
        }
        if(getIntent().getStringExtra("etn1")!=null) {
            j =Integer.parseInt( getIntent().getStringExtra("etn1"));
        }
        k=j+1;

        count0=getIntent().getIntExtra("count0",0);
        count1=getIntent().getIntExtra("count1",0);
        count2=getIntent().getIntExtra("count2",0);

        counting=findViewById(R.id.counting);
        textView=findViewById(R.id.tvText);
        textView.setText(str);

        btnPlay=findViewById(R.id.play);
        buttonNext=findViewById(R.id.next);
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int lang=textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int speech=textToSpeech.speak(str,TextToSpeech.QUEUE_FLUSH,null);
                btnPlay.setVisibility(View.GONE);
            }
        });

        if(count2==1&& count0==0&& count1==0){
            counting.setText("PAGE " + j);
            buttonNext.setVisibility(View.GONE);
        }
        if(count1==1&& count0==0&& count2==0){
            counting.setText("PAGE " + j);
        }
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                btnPlay.setVisibility(View.GONE);
                int speech=textToSpeech.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                if(count0==1 && count1==0 && count2==0) {
                    extractPDFFile(data, i);
                    counting.setText("PAGE " + i);
                    if (i >= page) {
                        Toast.makeText(ViewActivity.this, "Page limit ends", Toast.LENGTH_SHORT).show();
                        buttonNext.setVisibility(View.GONE);
                    } else {

                        i++;
                    }
                }
                else if(count1==1 && count0==0 && count2==0){

                    extractPDFFile(data, k);
                    counting.setText("PAGE " + k);
                    if (k >= Integer.parseInt(etn2Text)) {
                        Toast.makeText(ViewActivity.this, "Ends up", Toast.LENGTH_SHORT).show();
                        buttonNext.setVisibility(View.GONE);
                    } else {

                        k++;
                    }
                }
            }
        });
    }
    private void extractPDFFile(Uri uri, final int i){

//        Toast.makeText(this, "Please wait,processing...", Toast.LENGTH_LONG).show();
        try {
            inputStream=ViewActivity.this.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {


                try{
                    String fileContent="";
                    final StringBuilder builder=new StringBuilder();
                    PdfReader reader=null;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                        reader = new PdfReader(inputStream);
                        int pages=reader.getNumberOfPages();
                        fileContent= PdfTextExtractor.getTextFromPage(reader,i);

                        builder.append(fileContent);

                    }
                    reader.close();
                    //Only this part run on ui thread...
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textView.setText(builder.toString());
                            int speech=textToSpeech.speak(textView.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                    });
                    reader.close();


                }catch (IOException e){
                    Toast.makeText(ViewActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
    @Override
    protected void onDestroy() {

        textToSpeech.stop();
        textToSpeech.shutdown();
        super.onDestroy();
    }
}