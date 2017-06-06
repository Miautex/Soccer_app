package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.webkit.WebView;

public class SiteNoticeActivity extends BaseActivity {

    private WebView webview = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_notice);
        setTitle(R.string.title_activity_site_notice);
        webview = (WebView)findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.loadData(getString(R.string.site_notice), "text/html", "UTF-8");
    }
}
