package com.example.atry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button proceedBtn,upload;
    Spinner spinner;
    TextView text1,text2;
    EditText etn1,etn2;
    private final int CHOOSE_PDF_FROM_DEVICE=1001;
    int count0=0,count1=0,count2=0;
    InputStream inputStream;
    String str="";
    String mgs="";
    Uri dataPDF;
    int pages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        upload=findViewById(R.id.upload);
        proceedBtn=findViewById(R.id.proceed_btn);
        spinner=findViewById(R.id.spinner);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        etn1=findViewById(R.id.edit1);
        etn2=findViewById(R.id.edit2);

        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        etn1.setVisibility(View.GONE);
        etn2.setVisibility(View.GONE);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count0 == 1 && count1 == 0 && count2 == 0) {
                    callChooseFileFromDevice();
                }
                else if (count1 == 1 && count0 == 0 && count2 == 0) {
                    if (etn1.getText().toString().trim().isEmpty() || etn2.getText().toString().trim().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        callChooseFileFromDevice();

                    }
                }
                else if (count2 == 1 && count0 == 0 && count1 == 0) {
                    if (etn1.getText().toString().trim().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else {
                        callChooseFileFromDevice();

                    }
                }
            }
        });

        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.items,android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pages != 0 && !str.equals("")) {
                    if (count0 == 1 && count1 == 0 && count2 == 0) {
                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                        intent.putExtra("pages", pages);
                        intent.putExtra("str", str);
                        intent.putExtra("dataPDF", dataPDF);
                        intent.putExtra("count0", count0);
                        intent.putExtra("count1", count1);
                        intent.putExtra("count2", count2);
                        startActivity(intent);
                    }
                    else if (count1 == 1 && count0 == 0 && count2 == 0) {
                        if (etn1.getText().toString().trim().isEmpty() || etn2.getText().toString().trim().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            if(Integer.parseInt(etn2.getText().toString().trim())<=Integer.parseInt(etn1.getText().toString().trim())){
                                Toast.makeText(MainActivity.this, "Invalid inputs", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                                intent.putExtra("etn2", etn2.getText().toString().trim());
                                intent.putExtra("etn1", etn1.getText().toString().trim());
                                intent.putExtra("pages", pages);
                                intent.putExtra("str", str);
                                intent.putExtra("dataPDF", dataPDF);
                                intent.putExtra("count0", count0);
                                intent.putExtra("count1", count1);
                                intent.putExtra("count2", count2);
                                startActivity(intent);
                            }
                        }
                    }
                    else if (count2 == 1 && count0 == 0 && count1 == 0) {
                        if (etn1.getText().toString().trim().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                            intent.putExtra("etn1",etn1.getText().toString().trim());
                            intent.putExtra("pages", pages);
                            intent.putExtra("str", str);
                            intent.putExtra("dataPDF", dataPDF);
                            intent.putExtra("count0", count0);
                            intent.putExtra("count1", count1);
                            intent.putExtra("count2", count2);
                            startActivity(intent);
                        }
                    }
                }
                else{
                    if(mgs.isEmpty()){
                    Toast.makeText(MainActivity.this, "Upload any file", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, mgs, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            text1.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
            etn1.setVisibility(View.GONE);
            etn2.setVisibility(View.GONE);
            count0=1;
            count1=0;count2=0;
        }
        else if(position==1){
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
            etn1.setVisibility(View.VISIBLE);
            etn2.setVisibility(View.VISIBLE);
            etn1.setText("");
            etn2.setText("");
            text1.setText("From page");
            text2.setText("to");
            count1=1;
            count0=0;count2=0;
        }
        else if(position==2){
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.GONE);
            etn1.setVisibility(View.VISIBLE);
            etn2.setVisibility(View.GONE);
            text1.setText("Page");
            etn1.setText("");
            count2=1;
            count0=0;count1=0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void callChooseFileFromDevice(){
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,CHOOSE_PDF_FROM_DEVICE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_PDF_FROM_DEVICE && resultCode==RESULT_OK){
            if(data!=null){
                dataPDF=data.getData();
                extractPDFFile(data.getData()); //data.getData() is the path of the file

            }
        }

    }
    private void extractPDFFile(Uri uri){

        try {
            inputStream=MainActivity.this.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                int status=0;
                try{
                    String fileContent="";
                    final StringBuilder builder=new StringBuilder();
                    PdfReader reader=null;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                        reader = new PdfReader(inputStream);
                        pages=reader.getNumberOfPages();
                            if(count0==1&& count1==0 && count2==0) {
                                status=0;
                                fileContent = PdfTextExtractor.getTextFromPage(reader, 1);
                            }
                        else if(count1==1&& count0==0 && count2==0) {
                                if (pages < Integer.parseInt(etn1.getText().toString().trim()) ||
                                        pages < Integer.parseInt(etn2.getText().toString().trim())) {

                                    status=1;
                                } else {
                                    status=0;
                                    fileContent = PdfTextExtractor.getTextFromPage(reader, Integer.parseInt(etn1.getText().toString().trim()));
                                }
                            }
                        else if(count2==1&& count1==0 && count0==0) {
                                if (pages < Integer.parseInt(etn1.getText().toString().trim())) {
                                    status=1;

                                } else {
                                    status=0;
                                    fileContent = PdfTextExtractor.getTextFromPage(reader, Integer.parseInt(etn1.getText().toString().trim()));
                                }
                        }
                        builder.append(fileContent);

                    }
                    reader.close();
                    //Only this part run on ui thread...
                    final int finalStatus = status;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(finalStatus ==1) {
                                mgs="Page limit exceeded";
                            }
                            else{
                                str = builder.toString();
                            }

                        }
                    });
                    reader.close();


                }catch (IOException e){
                    Toast.makeText(MainActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
}