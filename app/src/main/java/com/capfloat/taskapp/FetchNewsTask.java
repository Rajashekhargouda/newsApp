package com.capfloat.taskapp;

import android.os.AsyncTask;

import com.capfloat.taskapp.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FetchNewsTask extends AsyncTask<Void,Void,Integer> {
    private DatabaseHelper databaseHelper;
    private DataFetchListener dataFetchListener;
    private String serverUrl;
    private int errorCode = 400;
    private int successCode = 200;

    FetchNewsTask(DataFetchListener listener,DatabaseHelper databaseHelper, String serverUrl){
        this.databaseHelper = databaseHelper;
        this.dataFetchListener = listener;
        this.serverUrl = serverUrl;
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        try {
            URL url = new URL(serverUrl);
            HttpsURLConnection httpsURLConnection =  (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() != 200)
                return errorCode;

            InputStream inputStream = httpsURLConnection.getInputStream();
            if (inputStream == null )
                return errorCode;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return deserializeNewsData(bufferedReader.readLine(),databaseHelper);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return errorCode;
        }catch (Exception e){
            e.printStackTrace();
            return errorCode;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dataFetchListener.onLoading();
    }

    @Override
    protected void onPostExecute(Integer statusCode) {
        super.onPostExecute(statusCode);
        if (statusCode == successCode){
            dataFetchListener.onSuccess();
        }else {
            dataFetchListener.onError();
        }
    }

    private int deserializeNewsData(String response,
                                    DatabaseHelper databaseHelper){
        try {
            JSONObject jsonResponseObject = new JSONObject(response);
            JSONArray jsonArray = jsonResponseObject.getJSONArray("articles");
            int arrayLength = jsonArray.length();
            for (int i=0;i<arrayLength;i++){
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String title = jsonObj.getString("title");
                String author = jsonObj.getString("author");
                String description = jsonObj.getString("description");
                databaseHelper.insertData(author,title,description);
            }
            return successCode;
        } catch (JSONException e) {
            e.printStackTrace();
            return errorCode;
        }catch (Exception e){
            e.printStackTrace();
            return errorCode;
        }

    }
}
