package io.github.untactorder.androidclient

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.untactorder.BuildConfig
import io.github.untactorder.R
import io.github.untactorder.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    public var isTabletMode: Boolean = false
    private lateinit var layout: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the splash screen transition.
        installSplashScreen()

        // Layout binding.
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        // Set Activity Guideline position.
        val displayMetrics = resources.displayMetrics
        if (BuildConfig.DEBUG) {
            println("Display Metrics: " + displayMetrics.widthPixels + "x" + displayMetrics.heightPixels)
        }
        if (displayMetrics.widthPixels / displayMetrics.heightPixels.toDouble() >= 0.85) {
            // toggle table mode variable
            isTabletMode = true
            // move guidelines
            layout.mainGuidelineStart.setGuidelinePercent(0.5f)
            layout.mainGuidelineEnd.setGuidelinePercent(0.5f)
            // attach ordermenu container to main body
            val constraintSet = ConstraintSet()
            constraintSet.clone(layout.mainBody)
            constraintSet.connect(R.id.main_container_ordermenu, ConstraintSet.TOP, R.id.main_body, ConstraintSet.TOP)
            constraintSet.connect(R.id.main_container_ordermenu, ConstraintSet.BOTTOM, R.id.main_body, ConstraintSet.BOTTOM)
            constraintSet.applyTo(layout.mainBody)
            // stretch ordermenu container - 이거 layout_constrainedHeight_min = wrap이 작동 안해서 한거
            // https://github.com/androidx/constraintlayout/issues/376
            layout.mainDivOrdermenuOrderplacementCenter.visibility = View.VISIBLE
            layout.mainTvOrdermenuOrderlistTitle.visibility = View.VISIBLE
            layout.mainListOrdermenuOrderlist.visibility = View.VISIBLE
            // fit margins
            var ordermenuParams = layout.mainContainerOrdermenu.layoutParams as ConstraintLayout.LayoutParams
            ordermenuParams.topMargin = dpToPixel(10)
            ordermenuParams.bottomMargin = dpToPixel(14)
            ordermenuParams.leftMargin = dpToPixel(10)
            (layout.mainContainerUserinfo.layoutParams as ConstraintLayout.LayoutParams).rightMargin = dpToPixel(10)
            setOnGlobalLayoutListener(layout.mainBody, fun() {
                layout.mainListOrdermenuOrderlist.minimumHeight = layout.mainBody.top - layout.mainHeader.bottom
                val thinSlogan = layout.mainTvSloganThin
                thinSlogan.setPadding(thinSlogan.paddingLeft,
                    thinSlogan.paddingTop + layout.mainBody.top - layout.mainHeader.bottom,
                    thinSlogan.paddingRight, thinSlogan.paddingBottom)
            })
        }
    }

    fun dpToPixel(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun setOnGlobalLayoutListener(view: View, operation: () -> Unit) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                operation()
                removeOnGlobalLayoutListener(view.viewTreeObserver, this)
            }
        })
    }

    private fun removeOnGlobalLayoutListener(observer: ViewTreeObserver?, listener: OnGlobalLayoutListener) {
        observer?.removeOnGlobalLayoutListener(listener)
    }
}