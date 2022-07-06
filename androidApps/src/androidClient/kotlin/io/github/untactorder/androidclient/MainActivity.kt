package io.github.untactorder.androidclient

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.github.untactorder.*
import io.github.untactorder.auth.UsimUtil.Companion.isUsimPermissionGranted
import io.github.untactorder.auth.UsimUtil.Companion.requestUsimPermission
import io.github.untactorder.data.Customer
import io.github.untactorder.databinding.ActivityMainBinding
import io.github.untactorder.manual.ManualDisplayActivity
import io.github.untactorder.manual.ProjectDetailViewActivity
import io.github.untactorder.network.findPosServerAtThisNetwork
import java.util.*


/**
 * MainActivity
 *
 * @author irack000
 */
class MainActivity : AppCompatActivity() {
    private var isTabletMode: Boolean = false
    private lateinit var layout: ActivityMainBinding
    private lateinit var qrScanActivityLauncher: ActivityResultLauncher<Intent>

    private val TAG = javaClass.simpleName
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

        // Get Customer Phone number.
        layout.mainTvUserinfoPhone.text = GlobalApplication.PHONE_NUMBER

        // Set QR Code Scanner Activity Launcher.
        qrScanActivityLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val qrData = result.data?.getStringExtra("value")
                if (qrData != null) {
                    printLog(TAG, qrData)
                    if (qrCodeParser(qrData)) {
                        //toNetThread.add(RequestType.TableCheck)
                    } else {
                        printLog(TAG, getString(R.string.at_qrsc_invalid_msg), true)
                    }
                }
            } else {
                printLog(TAG, getString(R.string.at_qrsc_cancel_msg), true)
            }
        }

        findPosServerAtThisNetwork(fun(found: Boolean, data: String) {
            if (found) {
                printLog(TAG, "POS Server found at this network.")
                printLog(TAG, "POS Server INFO: $data")
            } else if (data.isNotEmpty()) {
                printLog(TAG, "An error($data) occurred.")
            } else {
                printLog(TAG, "POS Server not found.")
            }
        })
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

    /**
     * show project information.
     */
    fun onProjectDetailButtonClicked(view: View) {
        startActivity(Intent(this, ProjectDetailViewActivity::class.java))
    }

    /**
     * show app manual information.
     */
    fun onManualButtonClicked(view: View) {
        startActivity(Intent(this, ManualDisplayActivity::class.java))
    }

    /**
     * show connected store information.
     */
    fun onStoreInfoWidgetClicked(view: View) {
        //startActivity(Intent(this, ConnectedStoreInformationActivity::class.java))
    }

    /**
     * show connected store information.
     */
    fun onMakeOrderButtonClicked(view: View) {
        startActivity(Intent(this, OrderListActivity::class.java))
    }

    /**
     * parse qr code
     */
    fun qrCodeParser(qrData: String): Boolean {
        val list = qrData.split(",")
        if (list.size == 3) {
            if (list[0].split("[.]").toTypedArray().size != 4) throw Exception()
            val port = list[1].toInt()
            val table = list[2].toInt()
            return true
        } else {
            return false
        }
    }





}