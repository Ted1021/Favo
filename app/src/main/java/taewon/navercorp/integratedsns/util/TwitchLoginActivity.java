package taewon.navercorp.integratedsns.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.logging.Logger;

import retrofit2.http.HEAD;
import taewon.navercorp.integratedsns.R;

import static taewon.navercorp.integratedsns.util.AppController.TWITCH_REDIRECT_URL;

public class TwitchLoginActivity extends AppCompatActivity {

    private WebView webView;
    private String mReqType, mReqUrl;

    private static final String REQ_LOGIN = "login";

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

        Log.d("FAVO_TEST", "it's too  late ! this is last test");
        Log.d("FAVO_TEST", "it's too  late ! this is last test");

        mReqType = getIntent().getStringExtra("REQ_TYPE");
        mReqUrl = getIntent().getStringExtra("REQ_URL");

        switch(mReqType){
            case REQ_LOGIN:
                requestAccessToken();
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
                    TwitchLoginActivity.this.finish();
                }
                return true;
            }
        });
    }
}
