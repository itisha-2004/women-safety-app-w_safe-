package com.example.w_safe;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



public class QuickDefenseTipsActivity extends AppCompatActivity {

    WebView webView1, webView2, webView3, webView4, webView5, webView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_defense_tips);

        // Initialize the WebView objects
        webView1 = findViewById(R.id.webView1);
        webView2 = findViewById(R.id.webView2);
        webView3 = findViewById(R.id.webView3);
        webView4 = findViewById(R.id.webView4);
        webView5 = findViewById(R.id.webView5);
        webView6 = findViewById(R.id.webView6);

        // Load the YouTube videos
        loadYouTubeVideo(webView1, "https://www.youtube.com/embed/9m-x64bKfR4");
        loadYouTubeVideo(webView2, "https://www.youtube.com/embed/T7aNSRoDCmg");
        loadYouTubeVideo(webView3, "https://www.youtube.com/embed/J9lZ9OHdahg");
        loadYouTubeVideo(webView4, "https://www.youtube.com/embed/m2uKwkaa6Vw");
        loadYouTubeVideo(webView5, "https://www.youtube.com/embed/9m-x64bKfR4");
        loadYouTubeVideo(webView6, "https://www.youtube.com/embed/yH6mU_fDHqY");
    }

    private void loadYouTubeVideo(WebView webView, String videoUrl) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(videoUrl); // Load the YouTube video URL in WebView

        // Show a toast message after the last video is displayed
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals(videoUrl)) {
                    // Show a toast message after the last video is displayed
                    if (view == webView6) {
                        Toast.makeText(QuickDefenseTipsActivity.this, "Thank you for visiting!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
