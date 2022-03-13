package io.github.untactorder.manual

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import io.github.untactorder.databinding.ActivityProjectDetailViewBinding


/**
 * ProjectDetailViewActivity
 * 시간이 없어서 웹뷰로 구현
 * @see "https://web-inf.tistory.com/34"
 * @see "https://eunoia3jy.tistory.com/16"
 */
class ProjectDetailViewActivity : AppCompatActivity() {
    private lateinit var layout: ActivityProjectDetailViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout binding.
        layout = ActivityProjectDetailViewBinding.inflate(layoutInflater)
        setContentView(layout.root)

        // 웹뷰 설정
        val mWebView = layout.projdetailviewWvBody
        mWebView.webViewClient = WebViewClient()  // 클릭시 새창 안뜨게
        val mWebSettings = mWebView.settings  // 세부 세팅 등록
        mWebSettings.javaScriptEnabled = true  // 자바 스크립트 허용
        mWebSettings.setSupportMultipleWindows(false)  // 새창 띄우기 허용 여부
        mWebSettings.javaScriptCanOpenWindowsAutomatically = false  // javascript가 window.open()을 사용할 수 있도록 설정
        mWebSettings.loadWithOverviewMode = true  // 메타태그 허용 여부
        mWebSettings.useWideViewPort = true  // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false)  // 화면 줌 허용 여부
        mWebSettings.builtInZoomControls = false  // 화면 확대 축소 허용 여부
        //mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN  // 컨텐츠 사이즈 맞추기(권장되지 않음)
        mWebSettings.cacheMode = WebSettings.LOAD_NO_CACHE  // 브라우저 캐시 허용 여부
        mWebSettings.domStorageEnabled = true  // 로컬 저장소 허용 여부
        mWebView.loadUrl("https://cuws.notion.site/e82c5c200ea642a98f36970e0b49b49b")
    }
}