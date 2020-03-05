package com.example.offerwalltask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.offerwalltask.model.ObjectListModel;
import com.example.offerwalltask.model.RecordListModel;
import com.example.offerwalltask.net.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final String KEY = "SAVE_INSTANCE_STEPS";

    private Button mBtnNext;
    private WebView mWebView;
    private AppCompatTextView mTvText;

    private List<RecordListModel> mRecordListModelList;
    private int steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if (savedInstanceState != null) {
            steps = savedInstanceState.getInt(KEY);
            steps--;
        }
        loadListObject();
    }

    private void init() {
        mBtnNext = findViewById(R.id.btn_next);
        mTvText = findViewById(R.id.tv_contents);
        mWebView = findViewById(R.id.web_view);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (steps > mRecordListModelList.size() - 1) {
                    steps = 0;
                }
                loadObjectFromRetrofit(steps);
            }
        });
    }

    private void loadListObject() {
        RetrofitService.getInstance().getJSONApi().getTrending().enqueue(new Callback<List<RecordListModel>>() {
            @Override
            public void onResponse(Call<List<RecordListModel>> call, Response<List<RecordListModel>> response) {
                mRecordListModelList = response.body();
                loadObjectFromRetrofit(steps);
            }

            @Override
            public void onFailure(Call<List<RecordListModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    private void loadObjectFromRetrofit(int position) {
        RetrofitService.getInstance().getJSONApi().getObject(mRecordListModelList
                .get(position)
                .getId()
                .toString())
                .enqueue(new Callback<ObjectListModel>() {
            @Override
            public void onResponse(Call<ObjectListModel> call, Response<ObjectListModel> response) {
                ObjectListModel objectResponse = response.body();
                if (objectResponse.getType().equals("text")) {
                    mWebView.setVisibility(View.GONE);
                    mTvText.setVisibility(View.VISIBLE);

                    mTvText.setText(objectResponse.getContents());
                } else {
                    mWebView.setVisibility(View.VISIBLE);
                    mTvText.setVisibility(View.GONE);

                    mWebView.setWebViewClient(new WebViewClient());
                    mWebView.getSettings().setLoadsImagesAutomatically(true);
                    mWebView.getSettings().setJavaScriptEnabled(true);
                    mWebView.getSettings().setDomStorageEnabled(true);
                    mWebView.loadUrl(objectResponse.getUrl());
                }
                steps++;
            }

            @Override
            public void onFailure(Call<ObjectListModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, steps);
    }
}
