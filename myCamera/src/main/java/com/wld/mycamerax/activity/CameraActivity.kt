package com.wld.mycamerax.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.camera.lifecycle.ProcessCameraProvider
import io.reactivex.rxjava3.disposables.Disposable
import android.os.Bundle
import com.wld.mycamerax.R
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlin.Throws
import android.graphics.Bitmap
import android.content.Intent
import android.app.Activity
import android.graphics.Rect
import android.util.Log
import android.view.*
import android.widget.*
import androidx.camera.core.*
import com.wld.mycamerax.util.*
import io.reactivex.rxjava3.core.Observable
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

class CameraActivity : AppCompatActivity() {
    private lateinit var previewView: PreviewView
    private lateinit var img_switch: ImageView
    private lateinit var ll_picture_parent: LinearLayout
    private lateinit var img_picture: ImageView
    private lateinit var focus_view: FocusView
    private lateinit var view_mask: View
    private lateinit var rl_result_picture: RelativeLayout
    private lateinit var img_picture_cancel: ImageView
    private lateinit var img_picture_save: ImageView
    private lateinit var rl_start: RelativeLayout
    private lateinit var tv_back: TextView
    private lateinit var img_take_photo: ImageView
    private lateinit var imageCapture: ImageCapture
    private lateinit var mCameraControl: CameraControl
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var mCameraParam: CameraParam
    private var front = false
    private lateinit var mTimerDisposable: Disposable
    private var title: String? = ""
    private lateinit var title_v_tv: TextView
    private lateinit var title_h_tv: TextView
    private lateinit var view_mask_v: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_camera)
        mCameraParam = intent.getParcelableExtra(CameraConstant.CAMERA_PARAM)!!
        title = intent.getStringExtra(CameraConstant.TITLE)
        requireNotNull(mCameraParam) { "CameraParam is null" }
        if (!Tools.checkPermission(this)) {
            Toast.makeText(this,"Need to have permission to take pictures and storage",Toast.LENGTH_SHORT).show()
            finish()
        }
        front = mCameraParam.isFront
        initView()
        setViewParam()
        intCamera()
    }

    private fun setViewParam() {
        //是否显示切换按钮
        if (mCameraParam.isShowSwitch) {
            img_switch.visibility = View.VISIBLE
            if (mCameraParam.switchSize != -1 || mCameraParam.switchLeft != -1 || mCameraParam.switchTop != -1) {
                val layoutParams = img_switch.layoutParams as ConstraintLayout.LayoutParams
                if (mCameraParam.switchSize != -1) {
                    layoutParams.height = mCameraParam.switchSize
                    layoutParams.width = layoutParams.height
                }
                if (mCameraParam.switchLeft != -1) {
                    layoutParams.leftMargin = mCameraParam.switchLeft
                }
                if (mCameraParam.switchTop != -1) {
                    layoutParams.topMargin = mCameraParam.switchTop
                }
                img_switch.layoutParams = layoutParams
            }
            if (mCameraParam.switchImgId != -1) {
                img_switch.setImageResource(mCameraParam.switchImgId)
            }
        } else {
            img_switch.visibility = View.GONE
        }

        //是否显示裁剪框
        if (mCameraParam.isShowMask) {
            view_mask.visibility = View.VISIBLE
            title_h_tv.visibility = View.VISIBLE
            title_v_tv.visibility = View.GONE
            view_mask_v.visibility = View.GONE
            if (mCameraParam.maskMarginLeftAndRight != -1 || mCameraParam.maskMarginTop != -1 || mCameraParam.maskRatioH != -1) {
                val layoutParams = view_mask.layoutParams as ConstraintLayout.LayoutParams
                if (mCameraParam.maskMarginLeftAndRight != -1) {
                    layoutParams.rightMargin = mCameraParam.maskMarginLeftAndRight
                    layoutParams.leftMargin = layoutParams.rightMargin
                }
                if (mCameraParam.maskMarginTop != -1) {
                    layoutParams.topMargin = mCameraParam.maskMarginTop
                }
                if (mCameraParam.maskRatioH != -1) {
                    Tools.reflectMaskRatio(
                        view_mask,
                        mCameraParam.maskRatioW,
                        mCameraParam.maskRatioH
                    )
                    //     Tools.reflectMaskRatio(view_mask, mCameraParam.getMaskRatioH(), mCameraParam.getMaskRatioW());
                }
                view_mask.layoutParams = layoutParams
            }
            if (mCameraParam.maskImgId != -1) {
                view_mask.setBackgroundResource(mCameraParam.maskImgId)
            }
        } else {
            view_mask.visibility = View.GONE
            title_h_tv.visibility = View.GONE
            title_v_tv.visibility = View.VISIBLE
            view_mask_v.visibility = View.VISIBLE
        }
        if (mCameraParam.backText != null) {
            tv_back.text = mCameraParam.backText
        }
        if (mCameraParam.backColor != -1) {
            tv_back.setTextColor(mCameraParam.backColor)
        }
        if (mCameraParam.backSize != -1) {
            tv_back.textSize = mCameraParam.backSize.toFloat()
        }
        if (mCameraParam.takePhotoSize != -1) {
            val size = mCameraParam.takePhotoSize
            val pictureCancelParams = img_picture_cancel.layoutParams
            pictureCancelParams.height = size
            pictureCancelParams.width = pictureCancelParams.height
            img_picture_cancel.layoutParams = pictureCancelParams
            val pictureSaveParams = img_picture_save.layoutParams
            pictureSaveParams.height = size
            pictureSaveParams.width = pictureSaveParams.height
            img_picture_save.layoutParams = pictureSaveParams
            val takePhotoParams = img_take_photo.layoutParams
            takePhotoParams.height = size
            takePhotoParams.width = takePhotoParams.height
            img_take_photo.layoutParams = takePhotoParams
        }
        focus_view.setParam(
            mCameraParam.focusViewSize,
            mCameraParam.focusViewColor,
            mCameraParam.focusViewTime,
            mCameraParam.focusViewStrokeSize,
            mCameraParam.cornerViewSize
        )
        if (mCameraParam.cancelImgId != -1) {
            img_picture_cancel.setImageResource(mCameraParam.cancelImgId)
        }
        if (mCameraParam.saveImgId != -1) {
            img_picture_save.setImageResource(mCameraParam.saveImgId)
        }
        if (mCameraParam.takePhotoImgId != -1) {
            img_take_photo.setImageResource(mCameraParam.takePhotoImgId)
        }
        if (mCameraParam.resultBottom != -1) {
            val resultPictureParams =
                rl_result_picture.layoutParams as ConstraintLayout.LayoutParams
            resultPictureParams.bottomMargin = mCameraParam.resultBottom
            rl_result_picture.layoutParams = resultPictureParams
            val startParams = rl_start.layoutParams as ConstraintLayout.LayoutParams
            startParams.bottomMargin = mCameraParam.resultBottom
            rl_start.layoutParams = startParams
        }
        if (mCameraParam.resultLeftAndRight != -1) {
            val pictureCancelParams =
                img_picture_cancel.layoutParams as RelativeLayout.LayoutParams
            pictureCancelParams.leftMargin = mCameraParam.resultLeftAndRight
            img_picture_cancel.layoutParams = pictureCancelParams
            val pictureSaveParams = img_picture_save.layoutParams as RelativeLayout.LayoutParams
            pictureSaveParams.rightMargin = mCameraParam.resultLeftAndRight
            img_picture_save.layoutParams = pictureSaveParams
        }
        if (mCameraParam.backLeft != -1) {
            val layoutParams = tv_back.layoutParams as RelativeLayout.LayoutParams
            layoutParams.leftMargin = mCameraParam.backLeft
            tv_back.layoutParams = layoutParams
        }
        Tools.reflectPreviewRatio(previewView, Tools.aspectRatio(this))
    }

    private fun initView() {
        previewView = findViewById(R.id.preview_view)
        img_switch = findViewById(R.id.img_switch)
        ll_picture_parent = findViewById(R.id.ll_picture_parent)
        img_picture = findViewById(R.id.img_picture)
        focus_view = findViewById(R.id.focus_view)
        view_mask = findViewById(R.id.view_mask)
        rl_result_picture = findViewById(R.id.rl_result_picture)
        img_picture_cancel = findViewById(R.id.img_picture_cancel)
        img_picture_save = findViewById(R.id.img_picture_save)
        rl_start = findViewById(R.id.rl_start)
        tv_back = findViewById(R.id.tv_back)
        img_take_photo = findViewById(R.id.img_take_photo)
        title_h_tv = findViewById(R.id.title_h)
        title_v_tv = findViewById(R.id.title_v)
        title_h_tv.setText(title)
        title_v_tv.setText(title)
        view_mask_v = findViewById(R.id.view_mask_v)

        //切换相机
        img_switch.setOnClickListener(View.OnClickListener { v: View? ->
            switchOrition()
            bindCameraUseCases()
        })

        //拍照成功然后点取消
        img_picture_cancel.setOnClickListener(View.OnClickListener { v: View? ->
            img_picture.setImageBitmap(null)
            rl_start.setVisibility(View.VISIBLE)
            rl_result_picture.setVisibility(View.GONE)
            ll_picture_parent.setVisibility(View.GONE)
            autoFocusTimer()
        })
        //拍照成功然后点保存
        img_picture_save.setOnClickListener(View.OnClickListener { v: View? -> savePicture() })
        //还没拍照就点取消
        tv_back.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        //点击拍照
        img_take_photo.setOnClickListener(View.OnClickListener { v: View? ->
            takePhoto(
                mCameraParam.pictureTempPath
            )
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            autoFocus(event.x.toInt(), event.y.toInt(), false)
        }
        return super.onTouchEvent(event)
    }

    private fun switchOrition() {
        front = !front
    }

    private fun intCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
            } catch (e: Exception) {
                Log.d("wld________", e.toString())
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val screenAspectRatio = Tools.aspectRatio(this)
        val rotation =
            if (previewView.display == null) Surface.ROTATION_0 else previewView.display.rotation
        val preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
        imageCapture = ImageCapture.Builder() //优化捕获速度，可能降低图片质量
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()
        // 在重新绑定之前取消绑定用例
        cameraProvider.unbindAll()
        val cameraOrition =
            if (front) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
        val cameraSelector = CameraSelector.Builder().requireLensFacing(cameraOrition).build()
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
        mCameraControl = camera.cameraControl
        //        mCameraInfo = camera.getCameraInfo();
//        autoFocus(outLocation[0] + (view_mask.getMeasuredWidth()) / 2, outLocation[1] + (view_mask.getMeasuredHeight()) / 2, true);
        autoFocusTimer()
    }

    private fun autoFocusTimer() {
        val outLocation = Tools.getViewLocal(view_mask)
        autoFocusCancel()
        mTimerDisposable = Observable.interval(0, 20, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                autoFocus(
                    outLocation[0] + view_mask.measuredWidth / 2,
                    outLocation[1] + view_mask.measuredHeight / 2,
                    true
                )
            }
    }

    private fun autoFocusCancel() {
        mTimerDisposable.dispose()
    }

    private fun takePhoto(photoFile: String) {
        // 保证相机可用
        autoFocusCancel()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(File(photoFile)).build()

        //  设置图像捕获监听器，在拍照后触发
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    rl_start.visibility = View.GONE
                    rl_result_picture.visibility = View.VISIBLE
                    ll_picture_parent.visibility = View.VISIBLE
                    val bitmap = Tools.bitmapClip(this@CameraActivity, photoFile, front)
                    img_picture.setImageBitmap(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("wld_____", "Photo capture failed: \${exc.message}", exception)
                }
            })
    }

    private fun savePicture() {
        var rect: Rect? = null
        if (mCameraParam.isShowMask) {
            val outLocation = Tools.getViewLocal(view_mask)
            rect = Rect(
                outLocation[0], outLocation[1],
                view_mask.measuredWidth, view_mask.measuredHeight
            )
        }
        Tools.saveBitmap(
            this,
            mCameraParam.pictureTempPath,
            mCameraParam.picturePath,
            rect,
            front
        )
        Tools.deletTempFile(mCameraParam.pictureTempPath)
        val intent = Intent()
        intent.putExtra(CameraConstant.PICTURE_PATH, mCameraParam.picturePath)
        setResult(RESULT_OK, intent)
        finish()
    }

    //https://developer.android.com/training/camerax/configuration
    private fun autoFocus(x: Int, y: Int, first: Boolean) {
//        MeteringPointFactory factory = previewView.getMeteringPointFactory();
        val factory: MeteringPointFactory =
            SurfaceOrientedMeteringPointFactory(x.toFloat(), y.toFloat())
        val point = factory.createPoint(x.toFloat(), y.toFloat())
        val action = FocusMeteringAction.Builder(
            point,
            FocusMeteringAction.FLAG_AF
        ) //                .disableAutoCancel()
            //                .addPoint(point2, FocusMeteringAction.FLAG_AE)
            // 3秒内自动调用取消对焦
            .setAutoCancelDuration(mCameraParam.focusViewTime.toLong(), TimeUnit.SECONDS)
            .build()
        //        mCameraControl.cancelFocusAndMetering();
        val future = mCameraControl.startFocusAndMetering(action)
        future.addListener({
            try {
                val result = future.get()
                if (result.isFocusSuccessful) {
                    focus_view.showFocusView(x, y)
                    if (!first && mCameraParam.isShowFocusTips) {
//                        Toast mToast = Toast.makeText(getApplicationContext(), mCameraParam.getFocusSuccessTips(this), Toast.LENGTH_LONG);
//                        mToast.setGravity(Gravity.CENTER, 0, 0);
//                        mToast.show();
                    }
                } else {
                    if (mCameraParam.isShowFocusTips) {
//                        Toast mToast = Toast.makeText(getApplicationContext(), mCameraParam.getFocusFailTips(this), Toast.LENGTH_LONG);
//                        mToast.setGravity(Gravity.CENTER, 0, 0);
//                        mToast.show();
                    }
                    focus_view.hideFocusView()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                focus_view.hideFocusView()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        autoFocusCancel()
        cameraProvider.unbindAll()
        //        if (cameraExecutor != null)
//            cameraExecutor.shutdown();
    }
}