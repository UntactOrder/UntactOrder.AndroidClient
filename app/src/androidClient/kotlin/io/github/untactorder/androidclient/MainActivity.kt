package io.github.untactorder.androidclient

import android.app.ActivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.untactorder.*
import io.github.untactorder.auth.UsimUtil.Companion.isUsimPermissionGranted
import io.github.untactorder.auth.UsimUtil.Companion.requestUsimPermission
import io.github.untactorder.databinding.ActivityMainBinding
import kotlin.system.exitProcess


/**
 * MainActivity
 *
 * @author irack000
 */
class MainActivity : AppCompatActivity() {
    private var isTabletMode: Boolean = false
    private lateinit var layout: ActivityMainBinding

    private val TAG = "MainActivity"
    private val REQUEST_CODE_USIM_PERMISSION = 3519016

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Request Permissions.
        showPermissionRequestDialog()

        // Layout binding.
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        // Set Activity Guideline & Widget size and position.
        adjustUiObjectAttributes()

        layout.mainTvUserinfoPhone.text = GlobalApplication.PHONE_NUMBER
    }

    private fun showPermissionRequestDialog() {
        if (!isUsimPermissionGranted()) {
            printLog(TAG, "Permission is not granted")
            printLog(TAG, "Request Usim Permission")
            requestUsimPermission(REQUEST_CODE_USIM_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_USIM_PERMISSION) {
            if (!isUsimPermissionGranted()) {
                printLog(TAG, getString(R.string.error_usim_permission_not_allowed), true);
                clearApplicationDataNQuit()
            } else {
                printLog(TAG, "Permission is granted")
                restartApplication()  // 전화번호가 바뀐 경우에 잘 작동하는지 확인이 필요함
            }
        }
    }

    /**
     * adjust the UI object attributes.
     */
    private fun adjustUiObjectAttributes() {
        val displayMetrics = resources.displayMetrics
        printLog("Display Metrics", "" + displayMetrics.widthPixels + "x" + displayMetrics.heightPixels)
        //// if tablet mode, set guideline to half of the screen width.
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
            //// let the slogan stay stuck to the user information container through the code below.
            constraintSet.setVerticalBias(R.id.main_container_slogan, 1.0f);
            constraintSet.applyTo(layout.mainBody)
            // fit margins
            var ordermenuParams = layout.mainContainerOrdermenu.layoutParams as ConstraintLayout.LayoutParams
            ordermenuParams.topMargin = dpToPixel(10)
            ordermenuParams.bottomMargin = dpToPixel(14)
            ordermenuParams.leftMargin = dpToPixel(10)
            (layout.mainContainerUserinfo.layoutParams as ConstraintLayout.LayoutParams).rightMargin = dpToPixel(10)
        }
        //// if there's some space left above the slogan, make the orderlist(recycler) visible.
        setOnGlobalLayoutListener(layout.mainBody, fun() {
            // stretch ordermenu container - 이거 layout_constrainedHeight_min = wrap이 작동 안해서 한거
            // https://github.com/androidx/constraintlayout/issues/376

            // expand ordermenu orderlist
            layout.mainListOrdermenuOrderlist.minimumHeight = layout.mainBody.top - layout.mainHeader.bottom
        })
    }
}