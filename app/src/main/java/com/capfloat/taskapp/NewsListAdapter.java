package com.capfloat.taskapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private ArrayList<NewsModel> newsModelArrayList;
    Context context;

    NewsListAdapter(Context context, ArrayList<NewsModel>newsModelArrayList){
        this.newsModelArrayList = newsModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_list,
                viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        NewsModel newsModel = newsModelArrayList.get(pos);
        viewHolder.txtTitle.setText(newsModel.getTitle());
        viewHolder.txtAuthor.setText(newsModel.getAuthor());
    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAuthor,txtTitle;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAuthor = itemView.findViewById(R.id.txt_author);
            txtTitle = itemView.findViewById(R.id.txt_title);
        }
    }
}
