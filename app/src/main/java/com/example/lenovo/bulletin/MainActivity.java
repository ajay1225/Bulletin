package com.example.lenovo.bulletin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    @SuppressLint("StaticFieldLeak")
    public static RecyclerView recyclerView;
    LinearLayout linearLayout;
    private int i = 0;
    private String bundleKey="mainState";
    private String bundleValue="main";
    ArrayList<NewsDetails> favList;
    public static int scrollPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.mainLayout);
        recyclerView = findViewById(R.id.id_recycler_country);
        String country = "in";
        String category = "";
        String sources = "";
        if(savedInstanceState!=null){
            if(savedInstanceState.getString(bundleKey).equals("main")){
                bundleValue="main";
                Snackbar.make(linearLayout, R.string.loading, Snackbar.LENGTH_LONG).show();
                country = getIntent().getStringExtra("country");
                category = getIntent().getStringExtra("category");
                sources = getIntent().getStringExtra("publish");
                MainJsonParsing jsonParsing = new MainJsonParsing(this, country, category, sources);
                jsonParsing.execute();

            }
            else {
                bundleValue="fav";
                getSupportLoaderManager().restartLoader(23,null,MainActivity.this);
            }
        }
        else {
            Snackbar.make(linearLayout, R.string.loading, Snackbar.LENGTH_LONG).show();
            country = getIntent().getStringExtra("country");
            category = getIntent().getStringExtra("category");
            sources = getIntent().getStringExtra("publish");
            MainJsonParsing jsonParsing = new MainJsonParsing(this, country, category, sources);
            jsonParsing.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.news_source:
                bundleValue="main";
                scrollPosition=0;
                Intent intent = new Intent(this, NewsSourceActivity.class);
                this.startActivity(intent);
                break;
            case R.id.fav:
                bundleValue="fav";
                scrollPosition=0;
             getSupportLoaderManager().restartLoader(21,null,MainActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Nullable
            @Override
            public Cursor loadInBackground() {
                Cursor cursor=null;
                cursor=getContentResolver().query(NewsContentContract.NewsEntry.CONTENT_URI,null,null,null,null);
                return cursor;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        favList=new ArrayList<>();
        if(cursor==null){
            Toast.makeText(this, R.string.no_news, Toast.LENGTH_SHORT).show();
            favList=null;
        }
        else {
            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                String url = cursor.getString(1);
                NewsDetails details = new NewsDetails();
                details.setUrlToImage(url);
                details.setTitle(title);
                favList.add(details);
            }
            if(favList.size()==0){
                Snackbar.make(linearLayout, R.string.no_news_in,Snackbar.LENGTH_LONG);
            }
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setAdapter(new FavAdapter(this, favList));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(bundleValue!=null)
         outState.putString(bundleKey,bundleValue);
        if(MainJsonParsing.layoutManager!=null)
            scrollPosition=MainJsonParsing.layoutManager.findFirstVisibleItemPosition();
    }
}