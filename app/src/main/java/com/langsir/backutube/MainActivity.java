package com.langsir.backutube;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.services.youtube.YouTube;

public class MainActivity extends AppCompatActivity {
    private WebView webViewMain;
    private Button btnMain;
    private Button btnMainT;
    private String TAG="webv";
    private int g=0;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webViewMain=findViewById(R.id.webViewMain);
        btnMain=findViewById(R.id.btnMain);
        btnMainT=findViewById(R.id.btnMainT);
        YouTube G;

        WebSettings webSettings=webViewMain.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);/*快取*/
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.supportMultipleWindows();

        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String cacheDirPath = getFilesDir().getAbsolutePath() + "G";
        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录
       webViewMain.loadUrl("https://su6ru.github.io/webtest/index");
        //webViewMain.loadUrl("file:///android_asset/index.html");
        webViewMain.addJavascriptInterface(MainActivity.this,"android");  /*js傳值給android*/
        setWebViewClient();
        setChromeClient();

        /*Intent intentService=new Intent(MainActivity.this,ServicePlay.class);
        intentService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentService.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intentService);
        }else {
            startService(intentService);
        }*/
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewMain.loadUrl("javascript:javacalljs()");
            }
        });
        btnMainT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewMain.loadUrl("javascript:javacalljswith(" + "'Android传过来的参数'" + ")");

            }
        });

    }

    @JavascriptInterface
    public void jsCallAndroid(){
        btnMain.setText("Js调用Android方法");
    }

    @JavascriptInterface
    public void jsCallAndroidArgs(String args){
        btnMain.setText(args);
    }

    private void setChromeClient() {
        webViewMain.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress<100){
                    String progress=newProgress+"%";
                    Log.d("az",progress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("az","****************");
                Log.d("az","title==="+title);
                Log.d("az","-----------------");

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                result.confirm();

                return super.onJsAlert(view, url, message, result);
            }
        });

    }

    private void setWebViewClient() {
        webViewMain.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {/*頁面開使載入*/
                super.onPageStarted(view, url, favicon);
                Log.d("az","aaaaa");
            }

            @Override
            public void onPageFinished(WebView view, String url) { /*頁面讀取結束*/
                super.onPageFinished(view, url);
                Log.d("az","webview="+view);

                Log.d("az","Finished");

            }

            @Override
            public void onLoadResource(WebView view, String url) {/*讀取每筆資源都會調用一次*/
                super.onLoadResource(view, url);
                Log.d("az","Load");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);/*讀取錯誤6.0以下*/
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);/*讀取錯誤6.0以上*/

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);/*ssl處理*/
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode==KeyEvent.KEYCODE_BACK)&&webViewMain.canGoBack()){
            webViewMain.goBack();
            Log.d("back","goin");
            Toast.makeText(MainActivity.this,"返回上一頁",Toast.LENGTH_SHORT).show();
            return true;
        }else {
            Log.d("back", "goout");
            Toast.makeText(MainActivity.this,"關閉",Toast.LENGTH_SHORT).show();

            return super.onKeyDown(keyCode, event);
        }
    }
}