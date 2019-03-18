package com.fleet247.driver.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.fleet247.driver.R;
import com.fleet247.driver.retrofit.ApiURLs;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorialWebView extends Fragment {


    View view;
    WebView tutorialWebView;
    ProgressBar progressBar;


    public TutorialWebView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_tutorial_web_view, container, false);
        tutorialWebView=view.findViewById(R.id.webview);
        progressBar=view.findViewById(R.id.tutorial_progressbar);

        tutorialWebView.loadUrl(ApiURLs.tutorialURL);

        tutorialWebView.getSettings().setJavaScriptEnabled(true);

        tutorialWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d("Progress",newProgress+"");
                if (newProgress==100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

}
