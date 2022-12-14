package com.wld.mycamerax.util

import android.graphics.Bitmap
import com.wld.mycamerax.util.BitmapUtils
import android.media.ExifInterface
import kotlin.Throws
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import java.io.*

object BitmapUtils {
    /**
     * 注释：设置拍照图片正确方向
     *
     * @param id
     * @param bitmap
     * @return
     */
    fun setTakePicktrueOrientation(id: Int, bitmap: Bitmap): Bitmap {
        //如果返回的图片宽度小于高度，说明FrameWork层已经做过处理直接返回即可
        var bitmap = bitmap
        if (bitmap.width < bitmap.height) {
            return bitmap
        }
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(id, info)
        bitmap = rotaingImageView(id, info.orientation, bitmap)
        return bitmap
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    fun readPictureDegree(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path!!)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 把相机拍照返回照片转正
     *
     * @param angle 旋转角度
     * @return bitmap 图片
     */
    @JvmStatic
    fun rotaingImageView(id: Int, angle: Int, bitmap: Bitmap): Bitmap {
        //矩阵
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        //加入翻转 把相机拍照返回照片转正
        if (id == 1) {
            matrix.postScale(-1f, 1f)
        }
        // 创建新的图片
        return Bitmap.createBitmap(
            bitmap, 0, 0,
            bitmap.width, bitmap.height, matrix, true
        )
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getBitmapFormPath(pathName: String?, width: Int, height: Int): Bitmap? {
        var input: InputStream = FileInputStream(pathName)
        //这一段代码是不加载文件到内存中也得到bitmap的真是宽高，主要是设置inJustDecodeBounds为true
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true //不加载到内存
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input.close()
        val originalWidth = onlyBoundsOptions.outWidth
        val originalHeight = onlyBoundsOptions.outHeight
        if (originalWidth == -1 || originalHeight == -1) return null
        var inSampleSize = 1
        if (originalHeight > height || originalWidth > width) {
            val halfH = originalHeight / 2
            val halfW = originalWidth / 2
            while (halfH / inSampleSize > height && halfW / inSampleSize > width) {
                inSampleSize *= 2
            }
        }
        onlyBoundsOptions.inSampleSize = inSampleSize
        onlyBoundsOptions.inJustDecodeBounds = false
        val heightRatio =
            Math.ceil((onlyBoundsOptions.outHeight / height.toFloat()).toDouble()).toInt()
        val widthRatio =
            Math.ceil((onlyBoundsOptions.outWidth / width.toFloat()).toDouble()).toInt()
        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                onlyBoundsOptions.inSampleSize = heightRatio
            } else {
                onlyBoundsOptions.inSampleSize = widthRatio
            }
        }
        onlyBoundsOptions.inJustDecodeBounds = false
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888
        input = FileInputStream(pathName)
        val bitmap = BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input.close()
        return compressImage(bitmap) //再进行质量压缩
    }

    fun compressImage(image: Bitmap?): Bitmap? {
        val baos = ByteArrayOutputStream()
        image!!.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset() //重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                baos
            ) //这里压缩options，把压缩后的数据存放到baos中
            options -= 10 //每次都减少10
            if (options <= 0) break
        }
        val isBm =
            ByteArrayInputStream(baos.toByteArray()) //把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * @param bitmap
     * @param path
     * @return
     */
    fun saveBitmap(bitmap: Bitmap, path: String?): Boolean {
        try {
            val file = File(path)
            val parent = file.parentFile
            if (!parent.exists()) {
                parent.mkdirs()
            }
            val fos = FileOutputStream(file)
            val b = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            return b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}