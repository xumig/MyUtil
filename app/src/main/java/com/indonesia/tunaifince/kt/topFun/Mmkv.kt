package com.indonesia.tunaifince.kt.topFun

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private inline fun <T> MMKV.delegate(
    key: String? = null,
    defaultValue: T,
    crossinline getter: MMKV.(String, T) -> T,
    crossinline setter: MMKV.(String, T) -> Boolean
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            setter(key ?: property.name, value)
        }
    }


fun MMKV.int(key: String? = null, defValue: Int = 0): ReadWriteProperty<Any, Int> {
    return delegate(key, defValue, MMKV::decodeInt, MMKV::encode)
}

fun MMKV.long(key: String? = null, defValue: Long = 0): ReadWriteProperty<Any, Long> {
    return delegate(key, defValue, MMKV::decodeLong, MMKV::encode)
}

fun MMKV.boolean(
    key: String? = null,
    defValue: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    return delegate(key, defValue, MMKV::decodeBool, MMKV::encode)
}

fun MMKV.double(key: String? = null, defValue: Double = 0.00): ReadWriteProperty<Any, Double> {
    return delegate(key, defValue, MMKV::decodeDouble, MMKV::encode)
}
fun MMKV.string(
    key: String? = null,
    defValue: String = ""
): ReadWriteProperty<Any, String?> {
    return delegate(key, defValue, MMKV::decodeString, MMKV::encode)
}
