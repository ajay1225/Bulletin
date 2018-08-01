package com.example.lenovo.bulletin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.Myholder> {
    Context context;
    List<NewsDetails> favNews;

    public FavAdapter(Context context, List<NewsDetails> favNews) {
        this.context = context;
        this.favNews = favNews;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        int id=R.layout.fav_list;
        LayoutInflater inflater=LayoutInflater.from(context);
        final View view=inflater.inflate(id,parent, false);
        return new FavAdapter.Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.link.setText(favNews.get(position).getTitle());
        Picasso.with(context).load(favNews.get(position).getUrlToImage()).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return favNews.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView link;
        public Myholder(View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.fav_news_poster);
            link=itemView.findViewById(R.id.fav_news_link);
        }
    }
}
