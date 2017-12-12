package specup.fanmind.left;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;

public class ProjectRegister extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_register);
        ActivityManager.getInstance().addActivity(this);

        setView();


    }

    private void setView() {

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://fanmaum.com/projects/make");
//        webView.invokeZoomPicker();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    public void onBack(View v) {
        ActivityManager.getInstance().deleteActivity(this);
        finish();
    }
}
