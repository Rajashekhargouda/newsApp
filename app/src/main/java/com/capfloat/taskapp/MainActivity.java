package com.capfloat.taskapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataFetchListener{
    String url = "https://newsapi.org/v2/everything?q=bitcoin&apiKey=b1e1a7a0caff4b0eae74eb7291f96698";

    DatabaseHelper databaseHelper;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<NewsModel> newsModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchNewsTask(this,databaseHelper,url).execute();

    }

    private void init(){
        databaseHelper = new DatabaseHelper(this);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_news);
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
        newsModelArrayList = databaseHelper.getPaginatedData(20,0);
        NewsListAdapter newsListAdapter = new NewsListAdapter(this,newsModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsListAdapter);
    }
}
