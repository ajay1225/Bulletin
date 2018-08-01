package com.example.lenovo.bulletin;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NEWSAdapter extends RecyclerView.Adapter<NEWSAdapter.Holder> {
    public Context context;
    public List<NewsDetails> newsList;
    public NEWSAdapter(Context context, List<NewsDetails> newsList) {
        this.context=context;
        this.newsList=newsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        int id=R.layout.news_list_view;
        LayoutInflater inflater=LayoutInflater.from(context);
        final View view=inflater.inflate(id,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.newsTilte.setText(newsList.get(position).getTitle());
        Picasso.with(context).load(newsList.get(position).getUrlToImage()).into(holder.newsPoster);

    }

    @Override
    public int getItemCount() {
        if (newsList==null)return 0;
        return newsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView newsPoster;
        TextView newsTilte;
        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            newsPoster=itemView.findViewById(R.id.news_poster);
            newsTilte=itemView.findViewById(R.id.news_title);
        }

        @Override
        public void onClick(View v) {
            int id=getAdapterPosition();
            NewsDetails resultNews=newsList.get(id);
            Intent intent=new Intent(context,NewsInDetailActivity.class);
            intent.putExtra("resultNews",resultNews);
            context.startActivity(intent);
        }
    }
}
