package com.capfloat.taskapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capfloat.taskapp.model.NewsModel;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NewsModel> newsModelArrayList;
    Context context;
    boolean shouldShowFooter;
    private final int ITEM = 0;
    private final int FOOTER = 1;

    NewsListAdapter(Context context, ArrayList<NewsModel>newsModelArrayList){
        this.newsModelArrayList = newsModelArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.item_news_list,
                    viewGroup,false);
            return new ItemViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.footer_layout,
                    viewGroup,false);
            return new FooterHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int pos) {
        NewsModel newsModel = newsModelArrayList.get(pos);
        if (getItemViewType(pos) == ITEM){
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            itemViewHolder.txtTitle.setText(newsModel.getTitle());
            itemViewHolder.txtAuthor.setText(newsModel.getAuthor());
            itemViewHolder.txtDescription.setText(newsModel.getDescription());

        }
    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == newsModelArrayList.size()-1 && shouldShowFooter)
            return FOOTER;
        else return ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtAuthor,txtTitle, txtDescription;
        View view_divider;
        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAuthor = itemView.findViewById(R.id.txt_author);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDescription = itemView.findViewById(R.id.txt_description);
            view_divider = itemView.findViewById(R.id.view_divider);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder{

        public FooterHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
