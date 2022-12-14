package com.indonesia.tunaifince.kt.baseConfig

object HttpConfiguration {


    /***正式环境Local_host */
    const val IP_URL = "http://43.129.40.194:8888/"
    const val URL_API = "/v1/"
    const val URL_AGREEMENT = "/agreement/"

    var UI_SHOW_URL = ""
    var REQUEST_URL =""

    var AGREEMENT_PRIVACY = "$UI_SHOW_URL$URL_AGREEMENT"+"PRIVACY"
    var AGREEMENT_USER= "$UI_SHOW_URL$URL_AGREEMENT"+"USER"


    //845168357
    /**
     * 密钥
     */
    const val API_KEY = "WUtITlhZZzVTc2NIanU0Yg=="
    const val TOKEN_KEY = "3oOFjwsKxoCK7jGgqYdWUlt7qHjQuCFU"
    const val TOKEN_NAME = "LZJYUK"
    const val GET_ = "PQTWNKZ"

    //Adjust
    const val ADJUST_KEY = "15hquzgh840w"
    const val FLURRY_API_KEY = "111111111111"
    const val MAIN_KEY = "homelist_loanexcess_page" // face
}