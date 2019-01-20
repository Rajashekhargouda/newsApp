package com.capfloat.taskapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.capfloat.taskapp.Utils.NetworkManager;
import com.capfloat.taskapp.database.DatabaseHelper;
import com.capfloat.taskapp.model.NewsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataFetchListener{
    String url = "https://newsapi.org/v2/everything?q=bitcoin&apiKey=b1e1a7a0caff4b0eae74eb7291f96698";

    DatabaseHelper databaseHelper;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView txtInternet;
    ArrayList<NewsModel> newsModelArrayList;
    int limit =10,offset =0;
    boolean isLoading;
    NewsListAdapter newsListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
       if (NetworkManager.isNetworkConnected(this)){
            new FetchNewsTask(this,databaseHelper,url).execute();
        }
        else {
           txtInternet.setVisibility(View.VISIBLE);

       }
    }

    private void init(){
        databaseHelper = new DatabaseHelper(this);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_news);
        txtInternet = findViewById(R.id.txt_no_internet);
    }

    @Override
    public void onSuccess() {
        progressBar.setVisibility(View.GONE);
        setUpRecyclerData();
   }

    @Override
    public void onError() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setUpRecyclerData(){
        newsModelArrayList = databaseHelper.getPaginatedData(limit,offset);
        newsListAdapter = new NewsListAdapter(this,newsModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsListAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading && totalItemCount < databaseHelper.getItemCount()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >=
                            totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreItems(offset+=10);
                    }
                }
            }
        });
    }

    private void loadMoreItems(int offset){
        newsListAdapter.shouldShowFooter =true;
        isLoading =true;
        newsModelArrayList.addAll(databaseHelper.getPaginatedData(10,offset));
        newsListAdapter.notifyDataSetChanged();
        isLoading =false;
        newsListAdapter.shouldShowFooter = false;
    }
}
