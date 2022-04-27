package com.newsapifiltertest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapifiltertest.NewsDetailActivity;
import com.newsapifiltertest.R;
import com.newsapifiltertest.modals.Articles;

import java.util.ArrayList;

public class NewsRcvAdapter extends RecyclerView.Adapter<NewsRcvAdapter.ViewHolder> {
    private ArrayList<Articles> articlesArrayList;
    private Context context;

    public NewsRcvAdapter(ArrayList<Articles> articlesArrayList, Context context) {
        this.articlesArrayList = articlesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsRcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_rcv_item,parent,false);
        return new NewsRcvAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsRcvAdapter.ViewHolder holder, int position) {
        Articles articles = articlesArrayList.get(position);
        holder.subtitleTV.setText(articles.getDescription());
        holder.titleTV.setText(articles.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewsDetailActivity.class);
                i.putExtra("title",articles.getTitle());
                i.putExtra("desc",articles.getDescription());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return articlesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTV,subtitleTV;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            titleTV = itemView.findViewById(R.id.tv_news_heading);
            subtitleTV = itemView.findViewById(R.id.tv_sub_title);
        }
    }
}
