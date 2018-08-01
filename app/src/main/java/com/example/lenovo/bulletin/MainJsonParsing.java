package com.example.lenovo.bulletin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainJsonParsing extends AsyncTask<Void,Void,Void> {
    String data="";
    private List<NewsDetails> newsList ;
    @SuppressLint("StaticFieldLeak")
    Context context;
    final String SEARCH_URL="https://newsapi.org/v2/top-headlines";
    private String country="in";
    private String category="";
    private String sources="";
    private static final String MY_KEY=BuildConfig.API_KEY;
    public static GridLayoutManager layoutManager;

    public MainJsonParsing(Context context,String country,String category,String sources) {
        this.context = context;
        this.country=country;
        this.sources=sources;
        this.category=category;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        layoutManager=new GridLayoutManager(context,2);
        MainActivity.recyclerView.setLayoutManager(layoutManager);
        MainActivity.recyclerView.setAdapter(new NEWSAdapter(context,newsList));
        MainActivity.recyclerView.scrollToPosition(MainActivity.scrollPosition);

    }

    private String makeSearchQuery() {


        Uri uri=Uri.parse(SEARCH_URL)
                .buildUpon()
                .appendQueryParameter("category",category)
                .appendQueryParameter("country",country)
                .appendQueryParameter("sources",sources)
                .appendQueryParameter("apiKey",MY_KEY).build();
        return String.valueOf(uri);
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String JSONLink = makeSearchQuery();
            URL url = new URL(JSONLink);
            Log.i("doInBackground: ", url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                data = data + line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject mainObject=new JSONObject(data);
            JSONArray jsonArray=mainObject.getJSONArray("articles");
            newsList=new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){
                NewsDetails newsDetails=new NewsDetails();
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                newsDetails.setAuthor(jsonObject.optString("author"));
                newsDetails.setTitle(jsonObject.optString("title"));
                newsDetails.setDescription(jsonObject.optString("description"));
                newsDetails.setUrl(jsonObject.optString("url"));
                newsDetails.setUrlToImage(jsonObject.optString("urlToImage"));
                newsDetails.setPublishedAt(jsonObject.optString("publishedAt"));
                newsList.add(newsDetails);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
