package com.example.autobrowser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class BrowserActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var edtUrl: EditText
    private lateinit var btnGo: Button
    private lateinit var btnYouTube: Button // Thêm biến cho nút YouTube

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các thành phần giao diện
        webView = findViewById(R.id.webView)
        edtUrl = findViewById(R.id.edtUrl)
        btnGo = findViewById(R.id.btnGo)
        btnYouTube = findViewById(R.id.btnYouTube) // Ánh xạ nút từ XML

        // Cấu hình tối ưu cho WebView trên xe hơi
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        
        // Hỗ trợ phát video tự động không cần người dùng click kích hoạt
        webSettings.mediaPlaybackRequiresUserGesture = false 

        // Giữ luồng duyệt web luôn ở trong ứng dụng
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Cập nhật lại thanh địa chỉ khi chuyển trang
                edtUrl.setText(url)
            }
        }

        // Xử lý sự kiện khi bấm nút "Đi" trên màn hình
        btnGo.setOnClickListener {
            navigateToUrl()
        }

        // Xử lý sự kiện khi bấm nút "Enter/Go" trên bàn phím ảo
        edtUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                navigateToUrl()
                true
            } else {
                false
            }
        }

        // Xử lý sự kiện khi nhấn nút Bookmark YouTube
        btnYouTube.setOnClickListener {
            webView.loadUrl("https://youtube.com")
            edtUrl.clearFocus()
        }

        // Trang chủ mặc định khi mở app
        webView.loadUrl("https://google.com")
    }

    // Hàm chuẩn hóa địa chỉ URL đầu vào
    private fun navigateToUrl() {
        var url = edtUrl.text.toString().trim()
        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            webView.loadUrl(url)
            edtUrl.clearFocus()
        }
    }

    // Tận dụng nút Back hệ thống của ô tô để quay lại trang web trước đó
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
