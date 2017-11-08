package taewon.navercorp.integratedsns.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import taewon.navercorp.integratedsns.R;

public class TwitchLoginActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitch_login);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("REQ_URL"));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("CHECK_URL", " >>>> " + view.getUrl());
                Log.d("CHECK_URL", " >>>> " + request.getUrl().toString());
                webView.loadUrl(request.getUrl().toString());
                return true;
            }
        });
    }
}
