package taewon.navercorp.integratedsns.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import taewon.navercorp.integratedsns.R;

import static taewon.navercorp.integratedsns.util.AppController.TWITCH_REDIRECT_URL;

public class TwitchWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String mReqType, mReqUrl;

    private static final String REQ_LOGIN = "login";
    private static final String REQ_VIDEO = "video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitch_login);

        initView();
        initData();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void initData(){

        mReqType = getIntent().getStringExtra("REQ_TYPE");
        mReqUrl = getIntent().getStringExtra("REQ_URL");

        switch(mReqType){
            case REQ_LOGIN:
                requestAccessToken();
                break;

            case REQ_VIDEO:
                playVideo();
                break;
        }
    }

    private void requestAccessToken(){

        webView.loadUrl(mReqUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                webView.setVisibility(View.GONE);
                webView.loadUrl(request.getUrl().toString());

                if(request.getUrl().toString().startsWith(TWITCH_REDIRECT_URL)){
                    Log.d("CHECK_TOKEN", request.getUrl().toString());
                    setResult(RESULT_OK, getIntent().putExtra("CALLBACK", request.getUrl().toString()));
                    TwitchWebViewActivity.this.finish();
                }
                return true;
            }
        });
    }

    private void playVideo() {
        webView.loadUrl(mReqUrl);
    }
}