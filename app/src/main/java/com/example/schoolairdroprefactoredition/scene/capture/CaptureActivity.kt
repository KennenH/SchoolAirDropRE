package com.example.schoolairdroprefactoredition.scene.capture

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.example.schoolairdroprefactoredition.R
import com.jaeger.library.StatusBarUtil
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import kotlinx.android.synthetic.main.activity_capture.*
import kotlinx.android.synthetic.main.custom_barcode_scanner.*

class CaptureActivity : AppCompatActivity(), TorchListener {

    companion object {
        const val TRANSLATION_DURATION = 2000L
        const val ALPHA_DURATION = 500L
    }

    private val capture by lazy {
        CaptureManager(this@CaptureActivity, zxing_barcode_scanner)
    }

    private val animSet by lazy {
        AnimatorSet()
    }

    private var isFlashOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_capture)
        StatusBarUtil.setTranslucentForImageView(this, 0, toolbar)
        BarUtils.setNavBarColor(this, getColor(R.color.blackAlways))
        BarUtils.setNavBarLightMode(this, false)
        setSupportActionBar(toolbar)

        zxing_barcode_scanner.setTorchListener(this)
        capture.apply {
            initializeFromIntent(intent, savedInstanceState)
            setShowMissingCameraPermissionDialog(false)
            decode()
        }

        scanner.post {
            initAnim()
        }
    }

    private fun initAnim() {
        scanner.alpha = 0f
        val initY = scanner.translationY
        val destY = initY + ScreenUtils.getAppScreenHeight() * 0.55f
        val translateDown = ObjectAnimator.ofFloat(scanner, "translationY", initY, destY)
        val alphaIn = ObjectAnimator.ofFloat(scanner, "alpha", 0f, 1f)
        val alphaOut = ObjectAnimator.ofFloat(scanner, "alpha", 1f, 0f)
        translateDown.duration = TRANSLATION_DURATION
        alphaIn.duration = ALPHA_DURATION
        alphaOut.duration = ALPHA_DURATION

        animSet.apply {
            interpolator = LinearInterpolator()
            play(translateDown).with(alphaIn)
            play(alphaOut).after(TRANSLATION_DURATION - ALPHA_DURATION)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    start()
                }
            })
            start()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()

        if (animSet.isPaused) {
            animSet.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()

        if (animSet.isRunning) {
            animSet.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()

        if (animSet.isRunning) {
            animSet.cancel()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return zxing_barcode_scanner.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    /**
     * Check if the device's camera has a Flashlight.
     *
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    fun switchFlashlight(v: View) {
        if (hasFlash()) {
            if (!isFlashOn) {
                zxing_barcode_scanner.setTorchOn()
            } else {
                zxing_barcode_scanner.setTorchOff()
            }
        } else {
            Toast.makeText(this, getString(R.string.noTorchTip), Toast.LENGTH_SHORT).show()
        }
    }

//    /**
//     * 除了扫描区以外的遮罩层颜色
//     */
//    private fun changeMaskColor() {
//        val rnd = Random()
//        val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
//        zxing_viewfinder_view.setMaskColor(color)
//    }

    override fun onTorchOn() {
        isFlashOn = true
        switch_flashlight.setImageResource(R.drawable.ic_flashlight_on_button)
    }

    override fun onTorchOff() {
        isFlashOn = false
        switch_flashlight.setImageResource(R.drawable.ic_flashlight_button)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}