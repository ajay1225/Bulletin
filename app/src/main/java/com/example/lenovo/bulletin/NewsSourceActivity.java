package com.example.lenovo.bulletin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class NewsSourceActivity extends AppCompatActivity {

    String countryValue="in";
    String categoryValue="technology";
    String publishValue="";
    String countryCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_source);
        Button countryNews=findViewById(R.id.getNews);

        Spinner countrySpinner = findViewById(R.id.country);
        countrySpinner.setSelection(1);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryValue =(String) parent.getItemAtPosition(position);
                countryCode=countryValue.substring(0,2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                    countryValue = null;
            }
        });
        Spinner categorySpinner = findViewById(R.id.category);
        categorySpinner.setSelection(1);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 categoryValue =(String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {   categoryValue=null;     }
        });
        countryNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("country", countryCode);
                    intent.putExtra("category", categoryValue);
                    intent.putExtra("publish", "");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
                else{
                    dialogue();
                }

            }
        });
        Spinner publisherSpinner = findViewById(R.id.news_source);
        publisherSpinner.setSelection(1);
        publisherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                publishValue= (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button publisherNews=findViewById(R.id.getnewsSource);
        publisherNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    if(publishValue!=getString(R.string.publisher_choo)|| !publishValue.equals("")){
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("country","");
                        intent.putExtra("category","");
                        intent.putExtra("publish",publishValue);
                        getApplicationContext().startActivity(intent);
                    }
                }
                else{
                    dialogue();
                }
            }
        });
    }
    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return !(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable());
    }
    private void dialogue(){
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewsSourceActivity.this);
            alertDialog.setTitle(R.string.info);
            alertDialog.setMessage(R.string.neteork_error);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
