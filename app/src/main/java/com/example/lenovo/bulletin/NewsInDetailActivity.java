package com.example.lenovo.bulletin;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

public class NewsInDetailActivity extends AppCompatActivity {

    TextView url,description,date;
    ImageView poster,add;
    int flag=0;
    AdView mAdView;
    NewsDetails resultNews;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_in_detail);
        url=findViewById(R.id.url);
        constraintLayout=findViewById(R.id.constraint);
        Snackbar.make(constraintLayout,R.string.appwidget_text,Snackbar.LENGTH_LONG);
        mAdView=findViewById(R.id.banner);
        MobileAds.initialize(this,"ca-app-pub-6336523906622902~6817819328");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F4301DE8C6A9E863BECB29858AC61E95")
                .build();

        mAdView.loadAd(adRequest);
        description=findViewById(R.id.description);
        poster=findViewById(R.id.poster);

        resultNews= (NewsDetails) getIntent().getSerializableExtra("resultNews");
        url.setText(resultNews.getUrl());
        if(resultNews.getDescription()==null||resultNews.getDescription()==""){
            Toast.makeText(this, R.string.no_desc, Toast.LENGTH_SHORT).show();
        }
        description.setText(resultNews.getDescription());
        if(resultNews.getUrlToImage()==null|| resultNews.getUrlToImage().equals("")){
            Toast.makeText(this, R.string.no_image, Toast.LENGTH_SHORT).show();
        }
        Picasso.with(this).load(resultNews.getUrlToImage()).into(poster);
        add=findViewById(R.id.add);

        final Cursor cursor=getContentResolver().query(Uri.parse(NewsContentContract.NewsEntry.CONTENT_URI+"/*"),null,NewsContentContract.NewsEntry.COL_1+" LIKE ?",new String[]{resultNews.getUrl()},null);
        assert cursor != null;
        if(cursor.getCount() == 0){
            flag=0;
        }
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(1).equals(resultNews.getTitle())){
                    flag=1;
                }
            }while(cursor.moveToNext());
        }
        if (cursor.getCount()>0) {
            add.setImageResource(R.drawable.bookmark_dark);
        } else {
            add.setImageResource(R.drawable.bookmark_border);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(NewsContentContract.NewsEntry.COL_1,resultNews.getUrl());
                contentValues.put(NewsContentContract.NewsEntry.COL_2,resultNews.getUrlToImage());
                if (cursor.getCount()>0) {
                    add.setImageResource(R.drawable.bookmark_border);
                    int deleteValue = getContentResolver().delete(Uri.parse(NewsContentContract.NewsEntry.CONTENT_URI+"/*"),NewsContentContract.NewsEntry.COL_1+" LIKE ?",new String[]{resultNews.getTitle()});
                    Toast.makeText(NewsInDetailActivity.this, R.string.remove, Toast.LENGTH_SHORT).show();
                } else {
                    add.setImageResource(R.drawable.bookmark_dark);
                    Uri uri = getContentResolver().insert(NewsContentContract.NewsEntry.CONTENT_URI, contentValues);
                    Toast.makeText(NewsInDetailActivity.this, R.string.inserted, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void widgetbutton(View view) {
        Toast.makeText(this, R.string.added_widget, Toast.LENGTH_SHORT).show();
        NewAppWidget nw=new NewAppWidget();
        String widgettext=getString(R.string.ti)+"\n"+resultNews.getTitle()+"\n"+getString(R.string.des)+"\n"+resultNews.getDescription();
        nw.newstext=widgettext;
    }
}
